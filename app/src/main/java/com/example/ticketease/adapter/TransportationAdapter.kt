package com.example.ticketease.adapter

import com.example.ticketease.TransportationItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.R

class TransportationAdapter(private val data: List<TransportationItem>) :
    RecyclerView.Adapter<TransportationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationTextView: TextView = itemView.findViewById(R.id.cusTimeTableListLocations)
        val vehicleNoTextView: TextView = itemView.findViewById(R.id.cusTimeTableListVehicleNo)
        val timeTextView: TextView = itemView.findViewById(R.id.cusTimeTableListTime)
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
    }

    override fun getItemCount(): Int {
        return data.size
    }
}