package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

class CustomerHome : AppCompatActivity() {

    private lateinit var cusAccountManagement : LinearLayout
    private lateinit var cusQR : LinearLayout
    private lateinit var cusBookNow : LinearLayout
    private lateinit var cusBookings : LinearLayout
    private lateinit var cusWallet : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

        cusAccountManagement = findViewById(R.id.cus_account_management)
        cusQR = findViewById(R.id.cus_qr)
        cusBookNow = findViewById(R.id.cus_calender)
        cusBookings = findViewById(R.id.cus_bookings)
        cusWallet = findViewById(R.id.cus_wallet)

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

        cusBookings.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerHome, CustomerMyBookings::class.java)
            startActivity(intent)
        }

        cusWallet.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerHome, CustomerVirtualWallet::class.java)
            startActivity(intent)
        }

    }
}