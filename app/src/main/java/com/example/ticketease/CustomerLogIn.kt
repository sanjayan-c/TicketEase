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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class CustomerLogIn : AppCompatActivity() {

    private lateinit var cusEdtEmail: EditText
    private lateinit var cusEdtPassword: EditText
    private lateinit var cusBtnLogin: TextView
    private lateinit var cusBtnRegister: TextView
    private lateinit var pwdVisible: ImageView
    private lateinit var guestLogin: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_log_in)

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
            //login(email,password)
            val intent=Intent(this,CustomerHome::class.java)
            startActivity(intent)
        }

        cusBtnRegister.setOnClickListener{
            val intent=Intent(this,CustomerSignUp::class.java)
            startActivity(intent)
        }

        guestLogin.setOnClickListener{
            val intent=Intent(this,CustomerHome::class.java)
            startActivity(intent)
        }


    }
//    private fun login(email:String,password:String){
//        userAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    //logging in
//                    val intent= Intent(this@Login,UserActivity::class.java)
//                    finish()
//                    startActivity(intent)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(this@Login, "Incorrect Username or Password", Toast.LENGTH_SHORT,).show()
//                }
//            }
//    }
}