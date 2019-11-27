package com.shengshijie.email

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object EMailUtils {
    fun sendMail(
        eMailInfo: EMailInfo,
        filePaths: List<String?>?,
        sendEmailListener: SendEmailListener?
    ) {
        try {
            val authenticator: Authenticator? = if (eMailInfo.isValidate) {
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(eMailInfo.userName, eMailInfo.password)
                    }
                }
            } else null
            with(MimeMessage(Session.getDefaultInstance(eMailInfo.properties, authenticator))) {
                setFrom(InternetAddress(eMailInfo.fromAddress))
                setRecipient(Message.RecipientType.TO, InternetAddress(eMailInfo.toAddress))
                eMailInfo.ccAddress.map {
                    InternetAddress(it)
                }.toTypedArray().apply {
                    addRecipients(MimeMessage.RecipientType.CC, this)
                }
                subject = eMailInfo.subject
                sentDate = Date()
                setText(eMailInfo.body)
                val multipart = MimeMultipart()
                MimeBodyPart().apply {
                    setText(eMailInfo.body)
                    multipart.addBodyPart(this)
                }
                filePaths?.filter { it != null && it.isNotEmpty() }
                    ?.map {
                        MimeBodyPart().apply {
                            attachFile(it)
                            multipart.addBodyPart(this)
                        }
                    }
                setContent(multipart)
                Transport.send(this)
            }
            sendEmailListener?.onSuccess()
        } catch (ex: Exception) {
            sendEmailListener?.onError(ex.message)
        }
    }
}