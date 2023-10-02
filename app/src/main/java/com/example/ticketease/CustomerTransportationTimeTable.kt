package com.example.ticketease

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.TransportationAdapter
import java.util.Calendar

class CustomerTransportationTimeTable : AppCompatActivity() {

    private lateinit var cusTransDateText: TextView
    private lateinit var cusTransStartSpinner: Spinner
    private lateinit var cusTransEndSpinner: Spinner
    private lateinit var cusTransMethodSpinner: Spinner
    private lateinit var cusTransMethodSelect: ImageView
    private lateinit var cusTransDateSelect: ImageView
    private lateinit var cusTransStartSelect: ImageView
    private lateinit var cusTransEndSelect: ImageView
    private var cusStartLocations: MutableList<String> = mutableListOf()
    private var cusEndLocations: MutableList<String> = mutableListOf()
    private lateinit var cusStartLocationsAdapter: ArrayAdapter<String>
    private lateinit var cusEndLocationsAdapter: ArrayAdapter<String>
    private var selectedStartLocation: String = ""
    private var selectedEndLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_transportation_time_table)

        val cusTransportationTimeTableBack = findViewById<ImageView>(R.id.cusTransportationTimeTableBack)

        cusTransportationTimeTableBack.setOnClickListener {
            // Start the CustomerAccountManagement activity
            val intent = Intent(this@CustomerTransportationTimeTable, CustomerHome::class.java)
            startActivity(intent)
        }

        // Travel method

        cusTransMethodSpinner = findViewById(R.id.cusTransMethodSpinner)
        cusTransMethodSelect = findViewById(R.id.cusTransMethodSelect)
        val travelMethods = listOf("Bus", "Train", "Car", "Bicycle")
        // Create an ArrayAdapter to populate the Spinner with travel methods
        val custravelMethodsAdapter = ArrayAdapter(this, R.layout.customer_timetable_dropdown, travelMethods)
        custravelMethodsAdapter.setDropDownViewResource(R.layout.customer_timetable_dropdown)
        // Set the adapter for the Spinner
        cusTransMethodSpinner.adapter = custravelMethodsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusTransMethodSelect.setOnClickListener {
            cusTransMethodSpinner.performClick() // Show the dropdown
        }

        // Travel Date

        cusTransDateSelect = findViewById(R.id.cusTransDateSelect)
        cusTransDateText = findViewById(R.id.cusTransDateText)

        // Set an OnClickListener on the ImageView to show the DatePickerDialog
        cusTransDateSelect.setOnClickListener {
            showDatePicker()
        }

        cusStartLocations = mutableListOf("Colombo", "Kandy", "Jaffna", "Nuwara-Eliya")
        cusEndLocations = mutableListOf("Colombo", "Kandy", "Jaffna", "Nuwara-Eliya")

        // Start place

        cusTransStartSpinner = findViewById(R.id.cusTransStartSpinner)
        cusTransStartSelect = findViewById(R.id.cusTransStartSelect)
        // Create an ArrayAdapter to populate the Spinner with travel methods
        cusStartLocationsAdapter = ArrayAdapter(this, R.layout.customer_start_location_dropdown, cusStartLocations)
        cusStartLocationsAdapter.setDropDownViewResource(R.layout.customer_start_location_dropdown)
        // Set the adapter for the Spinner
        cusTransStartSpinner.adapter = cusStartLocationsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusTransStartSelect.setOnClickListener {
            cusTransStartSpinner.performClick() // Show the dropdown
        }

        //End place

        cusTransEndSpinner = findViewById(R.id.cusTransEndSpinner)
        cusTransEndSelect = findViewById(R.id.cusTransEndSelect)
        // Create an ArrayAdapter to populate the Spinner with travel methods
        cusEndLocationsAdapter = ArrayAdapter(this, R.layout.customer_end_location_dropdown, cusEndLocations)
        cusEndLocationsAdapter.setDropDownViewResource(R.layout.customer_end_location_dropdown)
        // Set the adapter for the Spinner
        cusTransEndSpinner.adapter = cusEndLocationsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusTransEndSelect.setOnClickListener {
            cusTransEndSpinner.performClick() // Show the dropdown
        }


        // Transport Time Table
        val sampleData = listOf(
            TransportationItem("Colombo",  "Galle","NA - 9856", "10.15 A.M"),
            TransportationItem("Kandy",  "Jaffna","ND - 9556", "1.15 P.M"),
            TransportationItem("Jaffna",  "Galle","NA - 3216", "4.15 P.M"),
            TransportationItem("Trincomalee",  "Galle","NA - 6556", "7.15 P.M"),
            TransportationItem("Jaffna",  "Colombo","NA - 3216", "4.15 P.M"),
            TransportationItem("Kandy",  "Galle","NA - 6556", "7.15 P.M")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerTransport)
        val adapter = TransportationAdapter(sampleData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle date selection here
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                cusTransDateText.text = selectedDate
            },
            year, month, dayOfMonth
        )
        // Set a minimum date constraint (e.g., today's date)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // Show the DatePickerDialog
        datePickerDialog.show()
    }

}

