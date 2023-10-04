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
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class CustomerSignUp : AppCompatActivity() {

    private lateinit var cusEdtEmail: EditText
    private lateinit var cusEdtNic: EditText
    private lateinit var cusEdtPassword: EditText
    private lateinit var cusConfirmEdtPassword: EditText
    private lateinit var cusBtnLSignUp: TextView
    private lateinit var cusBtnLogin: TextView
    private lateinit var passwordsNotMatch: TextView
    private lateinit var pwdVisible: ImageView
    private lateinit var confirmpwdVisible: ImageView
    private lateinit var guestLogin: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_sign_up)

        cusEdtEmail=findViewById(R.id.cus_login_email)
        cusEdtNic=findViewById(R.id.cus_login_nic)
        cusEdtPassword=findViewById(R.id.cus_login_password)
        cusConfirmEdtPassword=findViewById(R.id.cus_login_confirm_password)
        cusBtnLSignUp=findViewById(R.id.cus_signup_button)
        cusBtnLogin=findViewById(R.id.cus_text_login)
        pwdVisible=findViewById(R.id.cusimgPasswordVisibility)
        confirmpwdVisible=findViewById(R.id.cusimgConfirmPasswordVisibility)
        passwordsNotMatch=findViewById(R.id.cus_passwords_not_match)
        guestLogin=findViewById(R.id.cus_signup_as_guest)

        val registerString = "Login"
        val mSpannableString = SpannableString(registerString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        cusBtnLogin.text = mSpannableString

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

        confirmpwdVisible.setOnClickListener {
            if (cusConfirmEdtPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                cusConfirmEdtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                confirmpwdVisible.setImageResource(R.drawable.visibility_off)
            } else {
                cusConfirmEdtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmpwdVisible.setImageResource(R.drawable.visibility)
            }
            // Move the cursor to the end of the text
            cusConfirmEdtPassword.setSelection(cusConfirmEdtPassword.text.length)
        }

        cusBtnLogin.setOnClickListener{
            val intent=Intent(this,CustomerLogIn::class.java)
            startActivity(intent)
        }

        cusBtnLSignUp.setOnClickListener{
            //val name = edtName.text.toString()
//            val email=edtEmail.text.toString()
//            val password=edtPassword.text.toString()
//            signUp(name,email,password)
            val password = cusEdtPassword.text.toString()
            val confirmPassword = cusConfirmEdtPassword.text.toString()

            if (password == confirmPassword) {
                // Passwords match, proceed with login
                val intent = Intent(this, CustomerHome::class.java)
                startActivity(intent)
            } else {
                // Passwords do not match, show an error message
                passwordsNotMatch.visibility = View.VISIBLE
            }
        }

        guestLogin.setOnClickListener{
            val intent=Intent(this,CustomerHome::class.java)
            startActivity(intent)
        }


    }

//
//    private fun signUp(name:String,email:String,password:String){
//        userAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
//                    addUserToDatabase(name,email,userAuth.currentUser?.uid!!,"user" )
//                    //navigate to home
//                    val intent= Intent(this@SignUp,UserActivity::class.java)
//                    finish()
//                    startActivity(intent)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(this@SignUp,"Some error has occured", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    private fun addUserToDatabase(name:String,email:String,uid:String,type:String){
//        userDbRef= FirebaseDatabase.getInstance().getReference()
//        userDbRef.child("user").child(uid).setValue(User(name, email, uid,type))
//    }
//
}