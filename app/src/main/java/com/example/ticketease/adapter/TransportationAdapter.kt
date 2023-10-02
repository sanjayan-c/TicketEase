package com.example.ticketease.adapter

import android.content.Intent
import com.example.ticketease.TransportationItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.CustomerTransportationBooking
import com.example.ticketease.R

class TransportationAdapter(private val data: List<TransportationItem>, private val selectedDate: String) :
    RecyclerView.Adapter<TransportationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationTextView: TextView = itemView.findViewById(R.id.cusTimeTableListLocations)
        val vehicleNoTextView: TextView = itemView.findViewById(R.id.cusTimeTableListVehicleNo)
        val timeTextView: TextView = itemView.findViewById(R.id.cusTimeTableListTime)
        val bookNowImageView: ImageView = itemView.findViewById(R.id.cusTimeTableListBookNow)
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

        // Set a click listener for the bookNowImageView
        holder.bookNowImageView.setOnClickListener {
            // Start the new activity here and pass data from the corresponding item
            val context = it.context
            val intent = Intent(context, CustomerTransportationBooking::class.java)
            intent.putExtra("startLocations", item.startLocations)
            intent.putExtra("endLocations", item.endLocations)
            intent.putExtra("vehicleNo", item.vehicleNo)
            intent.putExtra("time", item.time)
            intent.putExtra("selectedDate", selectedDate)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}