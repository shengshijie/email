package com.shengshijie.emailtest

import android.content.Context
import android.os.Build
import com.shengshijie.email.EMailInfo
import com.shengshijie.email.EMailUtils
import com.shengshijie.email.SendEmailListener
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

object FeedbackManager {
    private const val FROM_ADDRESS = "rongfutong@126.com"
    private const val FROM_ADDRESS_PWD = "rongfutong2020"
    private val FORMAT: Format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    fun sendFeedback(
        context: Context,
        toAddress: String?,
        ccAddress: Array<String> = arrayOf(),
        sendEmailListener: SendEmailListener?
    ) = thread {
        try {
            var filePaths: List<String?> = listOf()
            val eMailInfo = EMailInfo()
            val title = "RFT: " + Build.MODEL
            val sb = StringBuilder()
            try {
                val head = "************* Log ****************" +
                        "\nTime                      : " + FORMAT.format(Date(System.currentTimeMillis())) +
                        "\nDevice Manufacturer       : " + Build.MANUFACTURER +
                        "\nDevice Model              : " + Build.MODEL +
                        "\nAndroid Version           : " + Build.VERSION.RELEASE +
                        "\nAndroid SDK               : " + Build.VERSION.SDK_INT +
                        "\n************* Log ****************\n"
                sb.append(head)
            } catch (e: Exception) {
                sb.append("FEED CONTENT ERROR:")
                sb.append(e.message)
                sb.append("\n")
            }
            try {
                filePaths = context.getExternalFilesDir(null)?.listFiles()
                    ?.filter {
                        it != null && it.isFile && it.name.contains(
                            SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
                        )
                    }
                    ?.map { it.absolutePath }
                    ?.toList()
                    ?: listOf()
            } catch (e: Exception) {
                sb.append("FEED LOG ERROR:")
                sb.append(e.message)
                sb.append("\n")
            }
            eMailInfo.mailServerHost = "smtp.126.com"
            eMailInfo.mailServerPort = "25"
            eMailInfo.isValidate = true
            eMailInfo.userName = FROM_ADDRESS
            eMailInfo.password = FROM_ADDRESS_PWD
            eMailInfo.fromAddress = FROM_ADDRESS
            eMailInfo.subject = title
            eMailInfo.body = sb.toString()
            eMailInfo.toAddress = toAddress
            ccAddress.let { eMailInfo.ccAddress = ccAddress }
            EMailUtils.sendMail(eMailInfo, filePaths, sendEmailListener)
        } catch (e: Exception) {
            sendEmailListener?.onError(e.message)
        }
    }
}