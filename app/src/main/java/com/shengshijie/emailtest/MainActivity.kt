package com.shengshijie.emailtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.shengshijie.email.SendEmailListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun test(view: View) {
        FeedbackManager.sendFeedback(
            this,
            "850579014@qq.com", arrayOf("850579014@qq.com"),
            sendEmailListener = object : SendEmailListener {
                override fun onSuccess() {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "send success",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onError(message: String?) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}
