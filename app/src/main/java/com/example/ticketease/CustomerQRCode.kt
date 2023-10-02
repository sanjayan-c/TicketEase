package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class CustomerQRCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_qrcode)

        val cusQRBack = findViewById<ImageView>(R.id.cusQRBack)

        cusQRBack.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerQRCode, CustomerHome::class.java)
            startActivity(intent)
        }
    }
}