package com.shengshijie.email

interface SendEmailListener {
    fun onSuccess()
    fun onError(message: String?)
}