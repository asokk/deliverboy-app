package com.delivery.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.delivery.Activity.LogInActivity
import com.delivery.Config.BaseURL.IS_LOGIN
import com.delivery.Config.BaseURL.KEY_ID
import com.delivery.Config.BaseURL.KEY_NAME
import com.delivery.Config.BaseURL.PREFS_NAME
import com.delivery.Config.BaseURL.PREFS_NAME2
import java.util.*

class Session_management(var context: Context) {
    var prefs: SharedPreferences
    var prefs2: SharedPreferences
    var editor: SharedPreferences.Editor
    var editor2: SharedPreferences.Editor
    var PRIVATE_MODE = 0
    //Store Data
    fun createLoginSession(id: String?, name: String?) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_ID, id)
        editor.putString(KEY_NAME, name)
        editor.commit()
    }

    fun checkLogin() {
        if (!isLoggedIn) {
            val loginsucces = Intent(context, LogInActivity::class.java)
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // Add new Flag to start new Activity
            loginsucces.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(loginsucces)
        }
    }

    //Store And Use Data
    val userDetails: HashMap<String, String>
        get() {
            val user = HashMap<String, String>()
            user[KEY_ID] = prefs.getString(KEY_ID, null)
            user[KEY_NAME] = prefs.getString(KEY_NAME, null)
            return user
        }

    fun logoutSession() {
        editor.clear()
        editor.commit()
        val logout = Intent(context, LogInActivity::class.java)
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Add new Flag to start new Activity
        logout.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(logout)
    }

    // Get Login State
    val isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE)
        editor = prefs.edit()
        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE)
        editor2 = prefs2.edit()
    }
}