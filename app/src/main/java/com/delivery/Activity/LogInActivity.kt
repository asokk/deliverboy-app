package com.delivery.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.delivery.AppController
import com.delivery.Config.BaseURL
import com.delivery.MainActivity
import com.delivery.R
import com.delivery.util.CustomVolleyJsonRequest
import com.delivery.util.Session_management
import com.franmontiel.localechanger.LocaleChanger
import org.json.JSONException
import java.util.*

class LogInActivity : AppCompatActivity() {
    var Et_login_email: EditText? = null
    var Btn_Sign_in: RelativeLayout? = null
    var tv_login_email: TextView? = null
    var getemail: String? = null
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_log_in)
        //        String token1 = FirebaseInstanceId.getInstance().getToken();
//        String token = SharedPref.getString(LogInActivity.this,SharedPrefManager.getInstance(LogInActivity.this).getDeviceToken());
        Et_login_email = findViewById<View>(R.id.et_login_email) as EditText
        tv_login_email = findViewById<View>(R.id.tv_login_email) as TextView
        Btn_Sign_in = findViewById<View>(R.id.btn_Sign_in) as RelativeLayout
        getemail = Et_login_email!!.text.toString()
        Btn_Sign_in!!.setOnClickListener {
            if (getemail!!.isEmpty()) {
                Toast.makeText(this@LogInActivity, "Please Put Your Currect Email-Id", Toast.LENGTH_SHORT).show()
            } else {
                makejson()
            }
        }
    }

    fun makejson() {
        val tag_json_obj = "json_login_req"
        val UserName = Et_login_email!!.text.toString().trim { it <= ' ' }
        val params: MutableMap<String, String> = HashMap()
        params["user_password"] = UserName
        val jsonObjReq = CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN, params, Response.Listener { response ->
            try {
                val status = response.getString("responce")
                if (status.contains("true")) {
                    val jsonArray = response.getJSONArray("product")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val user_id = jsonObject.getString("id")
                        val user_fullname = jsonObject.getString("user_name")
                        val sessionManagement = Session_management(this@LogInActivity)
                        sessionManagement.createLoginSession(user_id, user_fullname)
                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Btn_Sign_in!!.isEnabled = false
                    }
                } else {
                    Btn_Sign_in!!.isEnabled = true
                    Toast.makeText(this@LogInActivity, "Please Put Correct Number", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> println("Error [$error]") })
        AppController.instance!!.addToRequestQueue(jsonObjReq, tag_json_obj)
    }
}