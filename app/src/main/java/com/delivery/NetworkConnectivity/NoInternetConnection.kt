package com.delivery.NetworkConnectivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.delivery.MainActivity
import com.delivery.NetworkConnectivity.NetworkConnection.connectionChecking
import com.delivery.R

class NoInternetConnection : AppCompatActivity() {
    var mCheckConnection: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_no_internet_connection)
        mCheckConnection = findViewById<View>(R.id.no_internet_connection) as ImageView
        mCheckConnection!!.setOnClickListener {
            if (connectionChecking(applicationContext)) {
                val intent = Intent(this@NoInternetConnection, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Check your connection.Try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}