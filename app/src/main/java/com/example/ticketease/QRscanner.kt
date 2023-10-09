package com.example.ticketease

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QRScanner : AppCompatActivity() {

   // private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        setContentView(R.layout.custom_scanner_layout
//        barcodeView = findViewById(R.id.barcode_scanner)
        // Other initialization code...
        startQRCodeScanner()
        // You can customize other elements in your layout, for example:
//        val customPromptTextView: TextView = findViewById(R.id.customPrompt)
//        customPromptTextView.text = "Scan the QR Code"
    }

    private fun startQRCodeScanner() {
        val integrator = IntentIntegrator(this)

        // Set the desired barcode formats to QR_CODE
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Customize other options as needed
       integrator.setPrompt(" ")
        integrator.setCameraId(0)  // Use the front camera (0) or back camera (1)
        integrator.setBeepEnabled(false)  // Play beep sound on successful scan

        // Force portrait mode
        integrator.setOrientationLocked(false)

        // Start the scanner
        integrator.initiateScan()
    }


    // Override onActivityResult to handle the result from the QR code scanner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // Handle the QR code content
                val qrContent = result.contents
                // You can process the QR code content as needed

                Log.d("QRScanner", "Scanned QR Code: $qrContent")

                val intent = Intent(this, Inspector_Done::class.java)

                // Pass the QR content as an extra to the intent
                intent.putExtra("QR_CONTENT", qrContent)

                // Start the next activity
                startActivity(intent)

            } else {
                // Handle case where QR code could not be scanned
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
