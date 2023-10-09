package com.example.ticketease.adapter

import android.content.Intent
import android.util.Log
import com.example.ticketease.data.CustomerTransportationItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.CustomerTransportationBooking
import com.example.ticketease.R
import com.google.firebase.auth.FirebaseAuth

class CustomerTransportationAdapter(private val data: List<CustomerTransportationItem>, private val selectedDate: String) :
    RecyclerView.Adapter<CustomerTransportationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationTextView: TextView = itemView.findViewById(R.id.cusTimeTableListLocations)
        val vehicleNoTextView: TextView = itemView.findViewById(R.id.cusTimeTableListVehicleNo)
        val timeTextView: TextView = itemView.findViewById(R.id.cusTimeTableListTime)
        val cusTimeTableListBookNowPart: LinearLayout = itemView.findViewById(R.id.cusTimeTableListBookNowPart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_transportation_timetable_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.locationTextView.text = item.startLocations+"-"+item.endLocations
        holder.vehicleNoTextView.text = "Vehicle No : "+item.vehicleNo
        holder.timeTextView.text = item.time
        if(FirebaseAuth.getInstance().currentUser != null) {
            holder.cusTimeTableListBookNowPart.visibility = View.VISIBLE
        }
        // Set a click listener for the bookNowImageView
        holder.cusTimeTableListBookNowPart.setOnClickListener {
            // Start the new activity here and pass data from the corresponding item
            val context = it.context
            val intent = Intent(context, CustomerTransportationBooking::class.java)
            intent.putExtra("startLocations", item.startLocations)
            intent.putExtra("endLocations", item.endLocations)
            intent.putExtra("vehicleNo", item.vehicleNo)
            intent.putExtra("time", item.time)
            intent.putExtra("selectedDate", selectedDate)
            intent.putExtra("routeNo", item.routeNo)
            Log.d("routeNo", item.routeNo)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}