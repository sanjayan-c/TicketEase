package com.example.ticketease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


class CustomerQRCode : AppCompatActivity(){

    private lateinit var imageCusQR : ImageView
    private lateinit var cusQRBack : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_qrcode)

        val intent = intent
        val cusId = intent.getStringExtra("cusId")

        cusQRBack = findViewById(R.id.cusQRBack)
        imageCusQR = findViewById(R.id.imageCusQR)

        cusQRBack.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
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