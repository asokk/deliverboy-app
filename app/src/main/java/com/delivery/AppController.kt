package com.delivery

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.delivery.util.ConnectivityReceiver
import com.delivery.util.ConnectivityReceiver.ConnectivityReceiverListener
import com.franmontiel.localechanger.LocaleChanger
import java.util.*

class AppController : Application() {
    private var mRequestQueue: RequestQueue? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        val locales: MutableList<Locale> = ArrayList()
        locales.add(Locale.ENGLISH)
        locales.add(Locale("ar", "ARABIC"))
        LocaleChanger.initialize(applicationContext, locales)
        LocaleChanger.setLocale(Locale.ENGLISH)
    }

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }
            return mRequestQueue
        }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue!!.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue!!.add(req)
    }

    fun cancelPendingRequests(tag: Any?) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    fun setConnectivityListener(listener: ConnectivityReceiverListener?) {
        ConnectivityReceiver.connectivityReceiverListener = listener
    }

    companion object {
        val TAG = AppController::class.java.simpleName
        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}