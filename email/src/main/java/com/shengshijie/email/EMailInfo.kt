package com.shengshijie.email

import java.util.*

class EMailInfo {
    var mailServerHost: String? = null
    var mailServerPort: String? = null
    var fromAddress: String? = null
    var toAddress: String? = null
    var ccAddress: Array<String> = arrayOf()
    var userName: String? = null
    var password: String? = null
    var isValidate = false
    var subject: String? = null
    var body: String? = null
    val properties: Properties
        get() {
            val prop = Properties()
            prop["mail.smtp.host"] = mailServerHost
            prop["mail.smtp.port"] = mailServerPort
            prop["mail.smtp.auth"] = if (isValidate) "true" else "false"
            prop["mail.smtp.starttls.enable"] = "true"
            return prop
        }

}