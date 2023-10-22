package com.example.ticketease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.InspectorTimeTable
import com.example.ticketease.R
import com.example.ticketease.data.InspectorJourneyItems


class InspectorJourneyAdapter (private var data: List<InspectorJourneyItems>,
) : RecyclerView.Adapter<InspectorJourneyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val BusRoute: TextView = itemView.findViewById(R.id.BusRoute)
        val Time: TextView = itemView.findViewById(R.id.BusTime)
        val passenger:TextView = itemView.findViewById(R.id.Passenger)
        val Income:TextView = itemView.findViewById(R.id.Income)
        val date:TextView = itemView.findViewById(R.id.TripDate)
        val cusMyBookingArrowUp: ImageView = itemView.findViewById(R.id.cusMyBookingArrowUp)
        val cusMyBookingArrowDown: ImageView = itemView.findViewById(R.id.cusMyBookingArrowDown)
        val cusMyBookingArrowUpLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowUpLayout)
        val cusMyBookingArrowDownLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowDownLayout)
        val cusMyBookingFullDetails: LinearLayout = itemView.findViewById(R.id.cusMyBookingFullDetails)
    //val StartTrip: Button = itemView.findViewById(R.id.StartTrip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.inspector_journey_history_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.BusRoute.text = item.startLocations + "-" + item.endLocations
        holder.Time.text = item.time
        holder.passenger.text = item.passenger
        holder.Income.text = item.income
        holder.date.text = item.date


        holder.cusMyBookingArrowDownLayout.setOnClickListener {
            holder.cusMyBookingFullDetails.visibility = View.VISIBLE
            holder.cusMyBookingArrowDownLayout.visibility = View.GONE
            holder.cusMyBookingArrowUpLayout.visibility = View.VISIBLE
        }

        holder.cusMyBookingArrowUpLayout.setOnClickListener {
            holder.cusMyBookingFullDetails.visibility = View.GONE
            holder.cusMyBookingArrowDownLayout.visibility = View.VISIBLE
            holder.cusMyBookingArrowUpLayout.visibility = View.GONE
        }
    }


}