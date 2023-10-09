package com.example.ticketease

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class InspectorAccountManagement: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_account)

        val profileImageFrame = findViewById<FrameLayout>(R.id.cusAccountProfileImageFrame)
        val profileEditFrame = findViewById<FrameLayout>(R.id.cusAccountProfileImageEditFrame)
        val cusAccManagementBack = findViewById<ImageView>(R.id.cusAccManagementBack)

        profileImageFrame.setOnClickListener {
            if (profileEditFrame.visibility == View.GONE) {
                // Show the edit frame
                profileEditFrame.visibility = View.VISIBLE
                val delayMillis = 5000 // 5000 milliseconds (5 seconds)

                val handler = Handler()
                handler.postDelayed({
                    // Hide the edit frame after the specified delay
                    profileEditFrame.visibility = View.GONE
                }, delayMillis.toLong())
            } else {
                // Hide the edit frame
                profileEditFrame.visibility = View.GONE
            }
        }

        cusAccManagementBack.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorAccountManagement, InspectorHome::class.java)
            startActivity(intent)
        }


    }
}