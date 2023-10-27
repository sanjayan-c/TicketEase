package com.example.ticketease.adapter

import android.util.Log
import android.view.FrameMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.R
import com.example.ticketease.data.CustomerMyBookingsItem
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerMyBookingsAdapter(private val data: List<CustomerMyBookingsItem>) :
    RecyclerView.Adapter<CustomerMyBookingsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cusMyBookingRoute: TextView = itemView.findViewById(R.id.cusMyBookingRoute)
        val cusMyBookingTime: TextView = itemView.findViewById(R.id.cusMyBookingTime)
        val cusMyBookingFullDetails: LinearLayout = itemView.findViewById(R.id.cusMyBookingFullDetails)
        val cusMyBookingVehicleNo: TextView = itemView.findViewById(R.id.cusMyBookingVehicleNo)
        val cusMyBookingRouteNo: TextView = itemView.findViewById(R.id.cusMyBookingRouteNo)
        val cusMyBookingNoOfPassengers: TextView = itemView.findViewById(R.id.cusMyBookingNoOfPassengers)
        val cusMyBookingSeatNo: TextView = itemView.findViewById(R.id.cusMyBookingSeatNo)
        val cusMyBookingIssuedDate: TextView = itemView.findViewById(R.id.cusMyBookingIssuedDate)
        val cusMyBookingIssuedTime: TextView = itemView.findViewById(R.id.cusMyBookingIssuedTime)
        val cusMyBookingArrowUp: ImageView = itemView.findViewById(R.id.cusMyBookingArrowUp)
        val cusMyBookingArrowDown: ImageView = itemView.findViewById(R.id.cusMyBookingArrowDown)
        val cusMyBookingArrowUpLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowUpLayout)
        val cusMyBookingArrowDownLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingArrowDownLayout)
        val cusMyBookingTicketDetails: FrameLayout = itemView.findViewById(R.id.cusMyBookingTicketDetails)
        val cusMyBookingDistance: TextView = itemView.findViewById(R.id.cusMyBookingDistance)
        val cusMyBookingCost: TextView = itemView.findViewById(R.id.cusMyBookingCost)
        val cusMyBookingDistanceLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingDistanceLayout)
        val cusMyBookingCostLayout: LinearLayout = itemView.findViewById(R.id.cusMyBookingCostLayout)
        val cusMyBookingTravelDateTextView: TextView = itemView.findViewById(R.id.cusMyBookingTravelDateTextView)
        val cusMyBookingArrowDownText: TextView = itemView.findViewById(R.id.cusMyBookingArrowDownText)
        val cusMyBookingArrowDownText2: TextView = itemView.findViewById(R.id.cusMyBookingArrowDownText2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_my_bookings_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.cusMyBookingRoute.text = item.startLocations + "-" + item.endLocations
        holder.cusMyBookingTime.text = item.time
        holder.cusMyBookingVehicleNo.text = item.vehicleNo
        holder.cusMyBookingRouteNo.text = item.routeNo
        holder.cusMyBookingNoOfPassengers.text = item.count
        holder.cusMyBookingSeatNo.text = item.seatNo
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy")
        val issuedDate = inputFormat.parse(item.issuedDate)
        holder.cusMyBookingIssuedDate.text = outputFormat.format(issuedDate)
        holder.cusMyBookingIssuedTime.text = item.issuedTime
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        Log.d("date", item.date)
        Log.d("currentDate",currentDate)

        // Compare the current date with the previous date
        if (position == 0 || item.date != data[position - 1].date) {
            // If it's the first item or the date has changed, show the date TextView
            holder.cusMyBookingTravelDateTextView.visibility = View.VISIBLE
            if (item.date == currentDate) {
                // If they match, show "Today" in the date TextView
                holder.cusMyBookingTravelDateTextView.text = "Today"
            } else {
                holder.cusMyBookingTravelDateTextView.text = item.date
            }
        }

        if (item.distance <= BigDecimal.ZERO) {
            if(item.distance < BigDecimal.ZERO){
                holder.cusMyBookingArrowDownText.text = "Ongoing"
                holder.cusMyBookingArrowDownText.setBackgroundResource(R.color.lt_green)
                holder.cusMyBookingArrowDownText.visibility = View.VISIBLE
                holder.cusMyBookingArrowDownText2.text = "Ongoing"
                holder.cusMyBookingArrowDownText2.setBackgroundResource(R.color.lt_green)
                holder.cusMyBookingArrowDownText2.visibility = View.VISIBLE
                holder.cusMyBookingTicketDetails.setBackgroundResource(R.drawable.cus_ticket_booking_greeen)
            }else if (item.date < currentDate) {
                holder.cusMyBookingArrowDownText.visibility = View.VISIBLE // Set your background drawable for past dates here
                holder.cusMyBookingArrowDownText2.visibility = View.VISIBLE // Set your background drawable for past dates here
            }else {
                holder.cusMyBookingTicketDetails.setBackgroundResource(R.drawable.cus_ticket_booking_greeen)
            }
        }else{
            holder.cusMyBookingDistanceLayout.visibility = View.VISIBLE
            holder.cusMyBookingCostLayout.visibility = View.VISIBLE
            //holder.cusMyBookingDistance.text = (item.distance/1000.0).toString() + " km"
            // Assuming item.distance is a Double or Float
            val distanceInKm = item.distance / BigDecimal(1000.0)
            holder.cusMyBookingDistance.text = "$distanceInKm km"
            holder.cusMyBookingCost.text = "LKR "+item.cost
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

    override fun getItemCount(): Int {
        return data.size
    }
}