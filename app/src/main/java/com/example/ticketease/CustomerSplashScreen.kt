package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class CustomerSplashScreen : AppCompatActivity() {

    private val SPLASH_TIMEOUT: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_splash_screen)

            // Delay for the specified time and then launch the main activity
            Handler().postDelayed({
                val intent = Intent(this, CustomerLogIn::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.cus_slide_up, 0)
                finish()
            }, SPLASH_TIMEOUT)
    }
}