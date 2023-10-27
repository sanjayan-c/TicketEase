package com.example.ticketease

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class CustomerLogIn : AppCompatActivity() {

    private lateinit var cusEdtEmail: EditText
    private lateinit var cusEdtPassword: EditText
    private lateinit var cusBtnLogin: TextView
    private lateinit var cusBtnRegister: TextView
    private lateinit var pwdVisible: ImageView
    private lateinit var guestLogin: LinearLayout
    private lateinit var userAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_log_in)

        userAuth=FirebaseAuth.getInstance()
        cusEdtEmail=findViewById(R.id.cus_login_email)
        cusEdtPassword=findViewById(R.id.cus_login_password)
        cusBtnLogin=findViewById(R.id.cus_login_button)
        cusBtnRegister=findViewById(R.id.cus_text_register)
        pwdVisible=findViewById(R.id.cusimgPasswordVisibility)
        guestLogin=findViewById(R.id.cus_login_as_guest)

        val registerString = "Register"
        val mSpannableString = SpannableString(registerString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        cusBtnRegister.text = mSpannableString

        pwdVisible.setOnClickListener {
            if (cusEdtPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                cusEdtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                pwdVisible.setImageResource(R.drawable.visibility_off)
            } else {
                cusEdtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                pwdVisible.setImageResource(R.drawable.visibility)
            }
            // Move the cursor to the end of the text
            cusEdtPassword.setSelection(cusEdtPassword.text.length)
        }

        cusBtnLogin.setOnClickListener{
            val email=cusEdtEmail.text.toString()
            val password=cusEdtPassword.text.toString()
            if(email != "" || password != "" ) {
                login(email, password)
            }else {
                Toast.makeText(this@CustomerLogIn, "Fill the above", Toast.LENGTH_SHORT,).show()
            }
        }

        cusBtnRegister.setOnClickListener{
            val intent=Intent(this,CustomerSignUp::class.java)
            startActivity(intent)
        }

        guestLogin.setOnClickListener{
            userAuth.signOut()
            val intent=Intent(this,CustomerHome::class.java)
            startActivity(intent)
        }


    }
    private fun login(email:String,password:String){
        userAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    val user = userAuth.currentUser
//                    if (user != null && user.isEmailVerified) {
                        // User is authenticated and their email is verified
                        Log.d(ContentValues.TAG, "signInWithEmail:success")

                        //logging in
                        val intent= Intent(this@CustomerLogIn,CustomerHome::class.java)
                        finish()
                        startActivity(intent)
//                    } else {
//                        // User is authenticated but their email is not verified
//                        Toast.makeText(
//                            this,
//                            "Please verify your email address first.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
                } else {
                    // Check the error message
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        if (errorMessage.contains("password")) {
                            // Incorrect password
                            Toast.makeText(
                                this,
                                "Incorrect password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (errorMessage.contains("no user record")) {
                            // Email not found
                            Toast.makeText(
                                this,
                                "No account found for this email",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Other error, show a generic message
                            Toast.makeText(
                                this,
                                "Login failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Unexpected error, show a generic message
                        Toast.makeText(
                            this,
                            "Login failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }
}