package com.example.ticketease

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.ticketease.data.ImageDataSingleton
import java.io.ByteArrayOutputStream
import java.sql.SQLException

class CustomerAccountManagement : AppCompatActivity() {

    private lateinit var viewInputFirstName: EditText
    private lateinit var viewInputLastName: EditText
    private lateinit var viewInputNIC: TextView
    private lateinit var viewInputGmail: TextView
    private lateinit var cusAccountProfileImage: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    var base64String: String? = null
    private var loadingAccManProgressBarLayout: FrameLayout? = null
    private var loadingAccManProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_account_management)

        val intent = intent
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val cusId = intent.getStringExtra("cusId")
        val cusNic = intent.getStringExtra("cusNic")
        val cusGmail = intent.getStringExtra("cusGmail")

        loadingAccManProgressBarLayout = findViewById(R.id.loadingAccManProgressBarLayout)
        loadingAccManProgressBar = findViewById(R.id.loadingAccManProgressBar)

        val profileImageFrame = findViewById<FrameLayout>(R.id.cusAccountProfileImageFrame)
        val profileEditFrame = findViewById<FrameLayout>(R.id.cusAccountProfileImageEditFrame)
        val cusAccManagementBack = findViewById<ImageView>(R.id.cusAccManagementBack)
        cusAccountProfileImage = findViewById(R.id.cusAccountProfileImage)
        val cusAccManageButton1 = findViewById<Button>(R.id.cusAccManageButton1)
        val cusAccManageButton2 = findViewById<Button>(R.id.cusAccManageButton2)
        viewInputFirstName = findViewById(R.id.editTextFirstName)
        viewInputLastName = findViewById(R.id.viewInputLastName)
        viewInputNIC = findViewById(R.id.viewInputNIC)
        viewInputGmail = findViewById(R.id.viewInputGmail)
        viewInputFirstName.text = Editable.Factory.getInstance().newEditable(firstName)
        viewInputLastName.text = Editable.Factory.getInstance().newEditable(lastName)
        viewInputNIC.text = cusNic
        viewInputGmail.text = cusGmail

        if (ImageDataSingleton.imageData != null) {
            // Decode the Base64 string to a Bitmap
            val decodedBytes = Base64.decode(ImageDataSingleton.imageData, Base64.DEFAULT)
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set the decoded Bitmap as the image for the ImageView
            cusAccountProfileImage.setImageBitmap(decodedBitmap)
        } else {
            // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
            cusAccountProfileImage.setImageResource(R.drawable.cus_image_not_found)
        }


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


        cusAccManageButton1.setOnClickListener {
            openGallery()
        }

        cusAccManageButton2.setOnClickListener {
            if(ImageDataSingleton.imageData!=null) {
                // Set ImageDataSingleton.imageData to null
                ImageDataSingleton.imageData = null

                // Create an AlertDialog
                val alertDialogBuilder = AlertDialog.Builder(this)

                // Set the dialog message and title
                alertDialogBuilder
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to remove the image?")

                // Add a "Cancel" button
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    // Dismiss the dialog if "Cancel" is clicked
                    dialog.dismiss()
                }

                // Add a "Confirm" button
                alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                    ImageDataSingleton.imageData = null
                    // If ImageDataSingleton.imageData is null, you can set a default image or do nothing
                    cusAccountProfileImage.setImageResource(R.drawable.cus_image_not_found)
                    // Dismiss the dialog
                    dialog.dismiss()
                }

                // Create and show the AlertDialog
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }

        // Assuming you have the necessary UI elements and variables for updated data
        val btnCusUpdate = findViewById<AppCompatButton>(R.id.btnCusUpdate)
        val btnCusCancel = findViewById<AppCompatButton>(R.id.btnCusCancel)
        btnCusUpdate.setOnClickListener {
            // Create an AlertDialog
            val alertDialogBuilder = AlertDialog.Builder(this)

            // Set the dialog message and title
            alertDialogBuilder
                .setTitle("Update Profile")
                .setMessage("Are you sure you want to update your profile?")

            // Add a "Cancel" button
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // Dismiss the dialog if "Cancel" is clicked
                dialog.dismiss()
            }

            // Add a "Confirm" button
            alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->

                runOnUiThread {
                    // Show the loading screen
                    loadingAccManProgressBarLayout?.visibility = View.VISIBLE
                    loadingAccManProgressBar?.visibility = View.VISIBLE
                    // Disable user interaction with the entire layout
                    loadingAccManProgressBarLayout?.isClickable = true
                    loadingAccManProgressBarLayout?.isFocusable = true
                }

                val cusConSQL = CusConSQL()
                cusConSQL.conclass { connection ->
                    if (connection != null) {
                        try {
                            // Update query with placeholders for binding
                            val query = "UPDATE customer SET cusFirstName = ?, cusLastName = ?, cusImage = ? WHERE cusId = ?"

                            val preparedStatement = connection.prepareStatement(query)
                            preparedStatement.setString(1, viewInputFirstName.text.toString())
                            preparedStatement.setString(2, viewInputLastName.text.toString())
                            preparedStatement.setString(3, ImageDataSingleton.imageData ) // Set the Base64-encoded image string
                            preparedStatement.setString(4, cusId) // Set the customer ID

                            // Execute the update query
                            preparedStatement.executeUpdate()

                            // Close the prepared statement
                            preparedStatement.close()
                            ImageDataSingleton.firstName = viewInputFirstName.text.toString()
                            ImageDataSingleton.lasttName = viewInputLastName.text.toString()
                            runOnUiThread {
                                // Hide the loading screen
                                loadingAccManProgressBarLayout?.visibility = View.GONE
                                loadingAccManProgressBar?.visibility = View.GONE
                                // Re-enable user interaction with the entire layout
                                loadingAccManProgressBarLayout?.isClickable = false
                                loadingAccManProgressBarLayout?.isFocusable = false

                                val intent = Intent(this@CustomerAccountManagement, CustomerHome::class.java)
                                startActivity(intent)
                            }
                            // Perform any UI updates or navigation as needed
                            // For example, show a success message or navigate to another screen
                        } catch (e: SQLException) {
                            Log.e("Update Error", "SQL Exception: ${e.message}")
                            e.printStackTrace()
                            // Handle any errors that occur during the update
                        } finally {
                            // Close the connection in the finally block to ensure it's always closed
                            connection.close()
                        }
                    } else {
                        Log.e("Update Error", "Database connection is null")
                        // Handle the case where the database connection is null
                    }
                }

                // Dismiss the dialog
                dialog.dismiss()
            }

            // Create and show the AlertDialog
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }


        btnCusCancel.setOnClickListener {
            finish()
        }

        cusAccManagementBack.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }
    }

        private fun openGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImageUri = data.data

                // Convert the selected image to Base64
                val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                val bytes = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                    bytes.write(buffer, 0, bytesRead)
                }
                val imageBytes: ByteArray = bytes.toByteArray()
                ImageDataSingleton.imageData = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                // Log the Base64-encoded image string
                Log.d("Base64ImageString", ImageDataSingleton.imageData !!)
                // Set the selected image to the ImageView
                cusAccountProfileImage.setImageURI(selectedImageUri)

                // Use the base64String as needed (e.g., store it in the database)
            }
        }
    }

    }