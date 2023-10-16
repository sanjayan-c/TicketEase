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
import com.example.ticketease.data.InspectorTimeTableItems

class InspectorTimetableAdapter(
    private var data: List<InspectorTimeTableItems>,
    private val onStartTripClickListener: InspectorTimeTable
) : RecyclerView.Adapter<InspectorTimetableAdapter.ViewHolder>() {

    interface OnStartTripClickListener {
        fun onStartTripClick(position: Int,sheduleId:String?)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val BusRoute: TextView = itemView.findViewById(R.id.BusRoute)
        val Time: TextView = itemView.findViewById(R.id.BusTime)
        val cusMyBookingArrowUp: ImageView = itemView.findViewById(R.id.cusMyBookingArrowUp)
        val cusMyBookingArrowDown: ImageView = itemView.findViewById(R.id.cusMyBookingArrowDown)
        val cusMyBookingArrowUpLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowUpLayout)
        val cusMyBookingArrowDownLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowDownLayout)
        val cusMyBookingFullDetails: LinearLayout = itemView.findViewById(R.id.cusMyBookingFullDetails)
        val StartTrip: Button = itemView.findViewById(R.id.StartTrip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.inspector_transportation_timetable_list,
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
        holder.BusRoute.text = item.startLocation + "-" + item.endLocation
        holder.Time.text = formatTimeRange(item.fromTime, item.toTime)


        holder.StartTrip.setOnClickListener {
            onStartTripClickListener.onStartTripClick(position,item.busScheduleId)
        }

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

    // Function to update the data and refresh the adapter
    fun updateData(newData: List<InspectorTimeTableItems>) {
        data = newData
        notifyDataSetChanged()
    }

    private fun formatTimeRange(fromTime: String?, toTime: String?): String {
        // Check for null values
        if (fromTime.isNullOrBlank() || toTime.isNullOrBlank()) {
            return "" // Handle null or blank values
        }

        // Split using any non-digit character
        val fromParts = fromTime.split("\\D+".toRegex())
        val toParts = toTime.split("\\D+".toRegex())

        // Check if the split resulted in three parts
        if (fromParts.size == 3 && toParts.size == 3) {
            val fromHour = fromParts[0].toInt()
            val fromMinute = fromParts[1].toInt()
            val fromSecond = fromParts[2].toInt()

            val toHour = toParts[0].toInt()
            val toMinute = toParts[1].toInt()

            val fromTimeStr = String.format("%02d:%02d", fromHour, fromMinute)
            val toTimeStr = String.format("%02d:%02d", toHour, toMinute)

            return "$fromTimeStr - $toTimeStr"
        }

        return "" // Handle invalid time format
    }

}
