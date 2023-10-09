package com.example.ticketease

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class InspectorScanner: AppCompatActivity()  {
    private val CAMERA_PERMISSION_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_scanner)

        // Request camera permission if not granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }

        // Set click listener for the scan button
        val scanButton: View = findViewById(R.id.scanButton)
        scanButton.setOnClickListener {
            // Check if camera permission is granted
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Start the QR code scanner
                startQRCodeScanner()
            } else {
                // Request camera permission if not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST
                )
            }
        }
    }

    private fun startQRCodeScanner() {
        val intent = Intent(this, QRScanner::class.java)

        // Start the SecondActivity
        startActivity(intent)

    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Handle result from QR code scanner
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents != null) {
//                // Handle the scanned QR code content (result.contents)
//                // You can display it, process it, etc.
//            } else {
//                // Handle the case where scanning was canceled or failed
//            }
//        }
//    }


}