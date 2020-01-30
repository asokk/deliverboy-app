package com.delivery.Config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    var prefs: SharedPreferences? = null
    @SuppressLint("ApplySharedPref")
    fun putBoolean(ctx: Context, key: String?, `val`: Boolean) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        prefs!!.edit().putBoolean(key, `val`).commit()
    }

    fun getBoolean(ctx: Context, key: String?, `val`: Boolean): Boolean {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        return prefs!!.getBoolean(key, false)
    }

    fun putInt(ctx: Context, key: String?, score: Int) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        prefs!!.edit().putInt(key, score).commit()
    }

    fun getInt(ctx: Context, key: String?): Int {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        return prefs!!.getInt(key, 0)
    }

    fun putString(ctx: Context, key: String?, score: String?) {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        prefs!!.edit().putString(key, score).commit()
    }

    fun getString(ctx: Context, key: String?): String {
        prefs = ctx.getSharedPreferences(BaseURL.APP_NAME, Context.MODE_PRIVATE)
        return prefs!!.getString(key, "")
    }
}