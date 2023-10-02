package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class CustomerHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

        val cusAccountManagement = findViewById<LinearLayout>(R.id.cus_account_management)
        val cusQR = findViewById<LinearLayout>(R.id.cus_qr)
        val cusBookNow = findViewById<LinearLayout>(R.id.cus_calender)

        cusAccountManagement.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerHome, CustomerAccountManagement::class.java)
            startActivity(intent)
        }

        cusQR.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerHome, CustomerQRCode::class.java)
            startActivity(intent)
        }

        cusBookNow.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerHome, CustomerTransportationTimeTable::class.java)
            startActivity(intent)
        }

    }
}