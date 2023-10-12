package com.example.ticketease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.R
import com.example.ticketease.data.CustomerTransactions
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class CustomerTransactionsAdapter (private val data: List<CustomerTransactions>) :
    RecyclerView.Adapter<CustomerTransactionsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customer_transactions_list_item_symbol: ImageView = itemView.findViewById(R.id.customer_transactions_list_item_symbol)
        val customer_transactions_list_item_name: TextView = itemView.findViewById(R.id.customer_transactions_list_item_name)
        val customer_transactions_list_item_date_time: TextView = itemView.findViewById(R.id.customer_transactions_list_item_date_time)
        val customer_transactions_list_item_no: TextView = itemView.findViewById(R.id.customer_transactions_list_item_no)
        val customer_transactions_list_item_cost: TextView = itemView.findViewById(R.id.customer_transactions_list_item_cost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_transactions_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.customer_transactions_list_item_name.text = item.detail
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = LocalDate.parse(item.date, formatter)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        holder.customer_transactions_list_item_date_time.text = formattedDate+" - "+item.time
        holder.customer_transactions_list_item_no.text = item.no
        val absPrice = item.price.abs()
        holder.customer_transactions_list_item_cost.text = "${absPrice} LKR"

        if (item.price > BigDecimal.ZERO) {
            // Set the ImageView source for positive-price transactions
            holder.customer_transactions_list_item_symbol.setImageResource(R.drawable.cus_transactions_in)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}