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

        cusAccountManagement.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Start the CustomerAccountManagement activity
                val intent = Intent(this@CustomerHome, CustomerAccountManagement::class.java)
                startActivity(intent)
            }
        })

        cusQR.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Start the CustomerAccountManagement activity
                val intent = Intent(this@CustomerHome, CustomerQRCode::class.java)
                startActivity(intent)
            }
        })
    }
}