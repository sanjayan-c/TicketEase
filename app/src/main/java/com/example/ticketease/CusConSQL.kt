package com.example.ticketease

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CusConSQL {

    @SuppressLint("NewApi")
    fun conclass(callback: (Connection?) -> Unit) {


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        GlobalScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connectURL = "jdbc:mysql://sql12.freesqldatabase.com/sql12653681?user=sql12653681&password=ISQmYl9qiE"
                connection = DriverManager.getConnection(connectURL)
            } catch (e: ClassNotFoundException) {
                Log.e("Error is from SQL", "JDBC Driver not found")
            } catch (e: SQLException) {
                Log.e("Error is from SQL", "SQL Exception: " + e.message)
                e.printStackTrace()
            } catch (e: Exception) {
                Log.e("Error is from SQL", "Unknown Exception: " + e.message)
                e.printStackTrace()
            }
            callback(connection)
        }
    }
}