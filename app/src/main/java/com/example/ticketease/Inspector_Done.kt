package com.example.ticketease

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Inspector_Done: AppCompatActivity()  {
    private lateinit var Qr_username : TextView
    private lateinit var startedTimeTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_done)
        Qr_username=findViewById(R.id.Qr_username)
        startedTimeTextView=findViewById(R.id.startedTimeTextView)
        val intent = intent
        if (intent.hasExtra("QR_CONTENT")) {
            val qrContent = intent.getStringExtra("QR_CONTENT")
            // Now, you have the QR content in the qrContent variable
            Log.d("NextActivity", "Received QR Content: $qrContent")
            Qr_username.text=qrContent

            val currentTime: LocalTime = LocalTime.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            // Format the LocalTime using the formatter
            val formattedTime: String = currentTime.format(formatter)
            startedTimeTextView.text=formattedTime



        }




    }
}