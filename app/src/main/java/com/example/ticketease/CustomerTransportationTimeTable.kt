package com.example.ticketease

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.CustomerTransportationAdapter
import com.example.ticketease.data.CustomerTransportationItem
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
    private var selectedDate: String = ""
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
        // Set an OnItemSelectedListener for the Spinner
        cusTransMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected travel method
                val selectedTravelMethod = travelMethods[position]
                // Log the selected travel method
                Log.d("SelectedTravelMethod", selectedTravelMethod)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
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
            CustomerTransportationItem("Colombo",  "Galle","NA - 9856", "10.15 A.M","100"),
            CustomerTransportationItem("Kandy",  "Jaffna","ND - 9556", "1.15 P.M","17"),
            CustomerTransportationItem("Jaffna",  "Galle","NA - 3216", "4.15 P.M","430"),
            CustomerTransportationItem("Trincomalee",  "Galle","NA - 6556", "7.15 P.M","65"),
            CustomerTransportationItem("Jaffna",  "Colombo","NA - 3216", "4.15 P.M","87"),
            CustomerTransportationItem("Kandy",  "Galle","NA - 6556", "7.15 P.M","25")
        )
        var cusTransSearch=findViewById<TextView>(R.id.cusTransSearch)
        cusTransSearch.setOnClickListener {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerTransport)
            Log.d("SelectedDate", selectedDate)
            val adapter = CustomerTransportationAdapter(sampleData, selectedDate)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

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
                //selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1) // Adding 1 because months are 0-indexed
                selectedDate = "$formattedDay/$formattedMonth/$selectedYear"
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

