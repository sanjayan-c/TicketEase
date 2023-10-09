package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.CustomerMyBookingsAdapter
import com.example.ticketease.data.CustomerMyBookingsItem
import com.example.ticketease.data.ImageDataSingleton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerMyBookings : AppCompatActivity() {

    private lateinit var cusMyBookingsBack : ImageView
    private lateinit var cusMyBookingsCustomerName : TextView
    private lateinit var cusMyBookingsCustomerNIC : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_my_bookings)

        cusMyBookingsBack = findViewById(R.id.cusMyBookingsBack)

        cusMyBookingsBack.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }

        cusMyBookingsCustomerName = findViewById(R.id.cusMyBookingsCustomerName)
        cusMyBookingsCustomerNIC = findViewById(R.id.cusMyBookingsCustomerNIC)

        cusMyBookingsCustomerName.text= ImageDataSingleton.firstName +" "+ImageDataSingleton.lasttName
        cusMyBookingsCustomerNIC.text = ImageDataSingleton.nic


        // Transport Time Table
            val sampleCusMyBookingsData = listOf(
                CustomerMyBookingsItem("Colombo", "Galle", "NA - 9856", "Route 1", "10.15 A.M", "2", "5, 6", "2023-10-08", "2023-10-03","420","1303"),
                CustomerMyBookingsItem("Kandy", "Jaffna", "ND - 9556", "Route 2", "1.15 P.M", "1", "3", "2023-10-04", "2023-10-03","",""),
                CustomerMyBookingsItem("Jaffna", "Galle", "NA - 3216", "Route 3", "4.15 P.M", "4", "1, 2, 3, 4", "2023-11-06", "2023-10-03","130","431"),
                CustomerMyBookingsItem("Trincomalee", "Galle", "NA - 6556", "Route 4", "7.15 P.M", "3", "7, 8, 9", "2023-10-07", "2023-10-03","310","1000"),
                CustomerMyBookingsItem("Jaffna", "Colombo", "NA - 3216", "Route 5", "4.15 P.M", "2", "10, 11", "2023-10-04", "2023-10-03","65","250")
            )
//        // Sort the sampleCusMyBookingsData list by the "date" property in descending order (latest first)
//        val sortedData = sampleCusMyBookingsData.sortedByDescending { it.date }
        // Get the current date in the format "yyyy-MM-dd"
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        // Sort the sampleCusMyBookingsData list using a custom comparator
        val sortedData = sampleCusMyBookingsData.sortedWith(compareByDescending<CustomerMyBookingsItem> {
            it.date == currentDate // First, check if the date matches the current date
        }.thenByDescending {
            it.date // Then, sort by date in descending order
        })

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerMyBookings)
        val adapter = CustomerMyBookingsAdapter(sortedData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}