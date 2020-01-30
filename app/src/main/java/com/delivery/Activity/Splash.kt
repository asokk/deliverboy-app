package com.delivery.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.delivery.MainActivity
import com.delivery.R
import com.delivery.util.Session_management
import com.franmontiel.localechanger.LocaleChanger

class Splash : AppCompatActivity() {
    private var dialog: AlertDialog? = null
    private var sessionManagement: Session_management? = null
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        sessionManagement = Session_management(this@Splash)
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2 * 1000.toLong())
                    checkAppPermissions()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }

    fun checkAppPermissions() {
        if ((ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_NETWORK_STATE)) {
                go_next()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE
                ),
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS)
            }
        } else {
            go_next()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                go_next()
            } else {
                val builder = AlertDialog.Builder(this@Splash)
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes") { dialog, id ->
                            // FIRE ZE MISSILES!
                            openPermissionScreen()
                        }
                        .setNegativeButton("Cancle") { dialog, id ->
                            // User cancelled the dialog
                            dialog.dismiss()
                        }
                dialog = builder.show()
            }
            return
        }
    }

    fun go_next() {
        if (sessionManagement!!.isLoggedIn) {
            val startmain = Intent(this@Splash, MainActivity::class.java)
            startActivity(startmain)
        } else {
            val startmain = Intent(this@Splash, LogInActivity::class.java)
            startActivity(startmain)
        }
        finish()
    }

    fun openPermissionScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", this@Splash.packageName, null))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102
    }
}