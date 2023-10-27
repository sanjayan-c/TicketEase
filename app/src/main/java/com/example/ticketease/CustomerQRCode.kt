package com.example.ticketease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CustomerQRCode : AppCompatActivity(){

    private lateinit var imageCusQR : ImageView
    private lateinit var cusQRBack : ImageView
    private lateinit var greenDownload : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_qrcode)

        val intent = intent
        val cusId = intent.getStringExtra("cusId")

        cusQRBack = findViewById(R.id.cusQRBack)
        imageCusQR = findViewById(R.id.imageCusQR)
        greenDownload = findViewById(R.id.greenDownload)

        cusQRBack.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
        greenDownload.setOnClickListener {
            // Get the Bitmap from the ImageView
            val qrBitmap = imageCusQR.drawable.toBitmap()

            // Create a download directory
            val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QR Codes")

            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            // Create a unique filename for the QR code image
            val fileName = "QRCode_${System.currentTimeMillis()}.png"

            // Create a file in the download directory
            val file = File(downloadDir, fileName)

            try {
                // Save the QR code image to the file
                val outputStream = FileOutputStream(file)
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

                // Tell the user that the download was successful
                Toast.makeText(this, "QR Code downloaded to Downloads/$fileName", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle any errors that occur during the download
                Toast.makeText(this, "Failed to download QR Code", Toast.LENGTH_SHORT).show()
            }
        }

        try {
            // Generate QR Code
            val bitmap = generateQRCode(cusId!!, 500, 500)

            // Set the generated QR code to the ImageView
            imageCusQR.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap {
        val multiFormatWriter = MultiFormatWriter()

        // Encode the content in a BitMatrix
        val bitMatrix: BitMatrix =
            multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height)

        // Create a Bitmap from the BitMatrix
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }
}