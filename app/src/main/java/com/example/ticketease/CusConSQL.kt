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
    var con: Connection? = null
    var uname: String? = null
    var pass: String? = null
    var ip: String? = null
    var port: String? = null
    var database: String? = null

    @SuppressLint("NewApi")
    fun conclass(callback: (Connection?) -> Unit) {
        ip = "192.168.1.73"
        port = "1433"
        database = "ticketease"
        uname = "sanjayan"
        pass = "123456"

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        GlobalScope.launch(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                val connectURL = "jdbc:mysql://db4free.net/ticket_ease?user=sanjayan_c&password=12345678"
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


//class CusConSQL {
//    var con: Connection? = null
//    var uname: String? = null
//    var pass: String? = null
//    var ip: String? = null
//    var port: String? = null
//    var database: String? = null
//
//    @SuppressLint("NewApi")
//    fun conclass(): Connection? {
//        ip = "192.168.1.73"
//        port = "1433"
//        database = "ticketease"
//        uname = "sanjayan"
//        pass = "123456"
//
//        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
//        var connection: Connection? = null
//        var connectURL: String? = null
//        try {
////            Class.forName("net.sourceforge.jtds.jdbc.Driver")
//            connectURL = "jdbc:mysql://db4free.net/ticket_ease?"+"user=sanjayan_c&password=12345678"
//            connection = DriverManager.getConnection(connectURL)
//        } catch (e: ClassNotFoundException) {
//            Log.e("Error is from SQL", "JDBC Driver not found")
//        } catch (e: SQLException) {
//            Log.e("Error is from SQL", "SQL Exception: " + e.message)
//            e.printStackTrace()
//        } catch (e: Exception) {
//            Log.e("Error is from SQL", "Unknown Exception: " + e.message)
//            e.printStackTrace()
//        }
//
//        return connection
//    }
//}