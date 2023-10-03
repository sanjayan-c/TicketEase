package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerTransportationBooking : AppCompatActivity() {

    private lateinit var cusBookingStart: TextView
    private lateinit var cusBookingEnd: TextView
    private lateinit var cusBookingDateText: TextView
    private lateinit var cusBookingCountSpinner: Spinner
    private lateinit var cusBookingCountSelect: ImageView
    private lateinit var btnCusBooking: AppCompatButton
    private lateinit var btnCusBookingConfirm: AppCompatButton
    private lateinit var btnCusBookingCancel: AppCompatButton
    private lateinit var cusBookingButtons: LinearLayout
    private lateinit var cusBookingButtons2: LinearLayout
    private lateinit var cusBookingButtonsTicketDetails: FrameLayout
    private var selectedSeatCount: Int? = null
    private var startLocations: String = ""
    private var endLocations: String = ""
    private var vehicleNo: String = ""
    private var time: String = ""
    private var selectedDate: String = ""
    private var routeNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_transportation_booking)

        val cusBookingBack = findViewById<ImageView>(R.id.cusBookingBack)

        cusBookingBack.setOnClickListener {
            finish()
        }

        cusBookingStart=findViewById(R.id.cusBookingStart)
        cusBookingEnd=findViewById(R.id.cusBookingEnd)
        cusBookingDateText=findViewById(R.id.cusBookingDateText)
        // Retrieve data from the intent
        val extras = intent.extras
        if (extras != null) {
            startLocations = extras.getString("startLocations", "")
            endLocations = extras.getString("endLocations", "")
            vehicleNo = extras.getString("vehicleNo", "")
            time = extras.getString("time", "")
            selectedDate = extras.getString("selectedDate", "")
            routeNo = extras.getString("routeNo","")
            // Set the retrieved data to your TextView elements
            cusBookingStart.text = "$startLocations"
            cusBookingEnd.text = "$endLocations"
            cusBookingDateText.text = "$selectedDate"
            // You can also use other data like vehicleNo and time if needed
        }

        cusBookingCountSpinner = findViewById(R.id.cusBookingCountSpin)
        cusBookingCountSelect = findViewById(R.id.cusBookingCountSelect)
        val cusBookSeatsCount = listOf(1,2,3,4,5)
        // Create an ArrayAdapter to populate the Spinner with travel methods
        val custravelMethodsAdapter = ArrayAdapter(this, R.layout.customer_timetable_dropdown, cusBookSeatsCount)
        custravelMethodsAdapter.setDropDownViewResource(R.layout.customer_timetable_dropdown)
        // Set the adapter for the Spinner
        cusBookingCountSpinner.adapter = custravelMethodsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusBookingCountSelect.setOnClickListener {
            cusBookingCountSpinner.performClick() // Show the dropdown
        }
        // Set an OnItemSelectedListener for the Spinner
        cusBookingCountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected travel method
                selectedSeatCount = cusBookSeatsCount[position]
                // Log the selected travel method
                Log.d("selectedSeatCount", selectedSeatCount.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Book button
        btnCusBooking=findViewById(R.id.btnCusBooking)
        cusBookingButtons2 = findViewById(R.id.cusBookingButtons2)
        cusBookingButtons = findViewById(R.id.cusBookingButtons)
        cusBookingButtonsTicketDetails = findViewById(R.id.cusBookingButtonsTicketDetails)

        btnCusBooking.setOnClickListener {
            cusBookingButtons.visibility = View.GONE
            cusBookingCountSelect.visibility = View.GONE
            cusBookingButtonsTicketDetails.visibility = View.VISIBLE
            cusBookingButtons2.visibility = View.VISIBLE

            var cusBookingRoute = findViewById<TextView>(R.id.cusBookingRoute)
            var cusBookingCustomerName = findViewById<TextView>(R.id.cusBookingCustomerName)
            var cusBookingCustomerNIC = findViewById<TextView>(R.id.cusBookingCustomerNIC)
            var cusBookingDepartureDate = findViewById<TextView>(R.id.cusBookingDepartureDate)
            var cusBookingDepartureTime = findViewById<TextView>(R.id.cusBookingDepartureTime)
            var cusBookingNoOfPassengers = findViewById<TextView>(R.id.cusBookingNoOfPassengers)
            var cusBookingVehicleNo = findViewById<TextView>(R.id.cusBookingVehicleNo)
            var cusBookingIssuedDate = findViewById<TextView>(R.id.cusBookingIssuedDate)
            var cusBookingRouteNo = findViewById<TextView>(R.id.cusBookingRouteNo)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Example format: "yyyy-MM-dd"
            // Get the current date and format it
            val issueDate = sdf.format(Date())
            cusBookingRoute.text = "$startLocations - $endLocations"
            cusBookingDepartureDate.text = selectedDate
            cusBookingDepartureTime.text = time
            cusBookingVehicleNo.text = vehicleNo
            cusBookingNoOfPassengers.text = selectedSeatCount.toString()
            cusBookingIssuedDate.text = issueDate
            cusBookingRouteNo.text = routeNo
        }

        btnCusBookingConfirm=findViewById(R.id.btnCusBookingConfirm)
        btnCusBookingConfirm.setOnClickListener {
            val intent = Intent(this@CustomerTransportationBooking, CustomerHome::class.java)
            startActivity(intent)
        }

        btnCusBookingCancel=findViewById(R.id.btnCusBookingCancel)
        btnCusBookingCancel.setOnClickListener {
            finish()
        }


    }
}