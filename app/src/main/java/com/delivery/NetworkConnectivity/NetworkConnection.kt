package com.delivery.NetworkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

object NetworkConnection {
    @JvmStatic
    fun connectionChecking(context: Context): Boolean {
        return try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) { // Get all state of network connection from connection manager
                val info = connectivity.activeNetworkInfo
                if (info != null && info.isConnected) {
                    return true
                }
            }
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun connectionType(context: Context): Boolean {
        try {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
                return if (info.type == ConnectivityManager.TYPE_WIFI) {
                    true
                } else {
                    when (info.subtype) {
                        TelephonyManager.NETWORK_TYPE_CDMA -> false
                        TelephonyManager.NETWORK_TYPE_IDEN -> false
                        TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                        else -> true
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            return false
        }
        return false
    }
}