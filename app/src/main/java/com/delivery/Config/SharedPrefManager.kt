package com.delivery.Config

import android.content.Context

class SharedPrefManager private constructor(private val mCtx: Context) {
    //this method will save the device token to shared preferences
    fun saveDeviceToken(token: String?): Boolean {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TAG_TOKEN, token)
        editor.apply()
        return true
    }

    //this method will fetch the device token from shared preferences
    val deviceToken: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(TAG_TOKEN, null)
        }

    companion object {
        private const val SHARED_PREF_NAME = "delivery"
        private const val TAG_TOKEN = "tagtoken"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance
        }
    }

}