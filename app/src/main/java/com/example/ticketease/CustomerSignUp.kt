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
import com.example.ticketease.data.Traveller
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.SQLException

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
    private lateinit var userAuth: FirebaseAuth
    private lateinit var userDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_sign_up)

        userAuth=FirebaseAuth.getInstance()
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
            val password = cusEdtPassword.text.toString()
            val confirmPassword = cusConfirmEdtPassword.text.toString()

            if (password == confirmPassword) {
                val nic = cusEdtNic.text.toString()
                val email=cusEdtEmail.text.toString()
                val password=cusEdtPassword.text.toString()
                signUp(nic,email,password)
            } else {
                // Passwords do not match, show an error message
                passwordsNotMatch.visibility = View.VISIBLE
            }
        }

        guestLogin.setOnClickListener{
            userAuth.signOut()
            val intent=Intent(this,CustomerHome::class.java)
            startActivity(intent)
        }


    }


    private fun signUp(nic:String,email:String,password:String){
        userAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    addUserToDatabase(nic,email,userAuth.currentUser?.uid!!,"user" )
                    addUserToSqlDatabase(nic, email, userAuth.currentUser?.uid!!)
                    //navigate to home
                    val intent= Intent(this@CustomerSignUp,CustomerHome::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@CustomerSignUp,"Some error has occured", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(nic:String,email:String,uid:String,type:String){
        userDbRef= FirebaseDatabase.getInstance().getReference()
        userDbRef.child("customer").child(uid).setValue(Traveller(nic, email, uid,type))
    }

    private fun addUserToSqlDatabase(nic: String, email: String, uid: String) {
        val cusConSQL = CusConSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                try {
                    val query = "INSERT INTO customer (cusNic, cusFirstName, cusLastName, cusGmail, cusId) " +
                            "VALUES (?, ?, ?, ?, ?)"

                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, nic)
                    preparedStatement.setNull(2, java.sql.Types.VARCHAR) // cusFirstName
                    preparedStatement.setNull(3, java.sql.Types.VARCHAR) // cusLastName
                    preparedStatement.setString(4, email)
                    preparedStatement.setString(5, uid)

                    // Execute the prepared statement
                    preparedStatement.executeUpdate()

                    // Close the prepared statement
                    preparedStatement.close()
                } catch (e: SQLException) {
                    Log.e("addUserToDatabase", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                Log.e("addUserToDatabase", "Database connection is null")
            }
        }
    }




}