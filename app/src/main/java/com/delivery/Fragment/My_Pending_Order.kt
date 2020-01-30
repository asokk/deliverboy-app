package com.delivery.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.*
import com.delivery.Adapter.My_Pending_Order_adapter
import com.delivery.AppController
import com.delivery.Config.BaseURL
import com.delivery.MainActivity
import com.delivery.Model.My_Pending_order_model
import com.delivery.R
import com.delivery.util.ConnectivityReceiver
import com.delivery.util.CustomVolleyJsonArrayRequest
import com.delivery.util.Session_management
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class My_Pending_Order : Fragment() {
    private var rv_myorder: RecyclerView? = null
    private var my_order_modelList: List<My_Pending_order_model> = ArrayList()
    private var sessionManagement: Session_management? = null
    var get_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_pending_order, container, false)
        sessionManagement = Session_management(activity!!)
        get_id = sessionManagement!!.userDetails[BaseURL.KEY_NAME]
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { v, keyCode, event ->
            // check user can press back button or not
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                true
            } else false
        }
        rv_myorder = view.findViewById<View>(R.id.rv_myorder) as RecyclerView
        rv_myorder!!.layoutManager = LinearLayoutManager(activity)
        if (ConnectivityReceiver.isConnected) {
            makeGetOrderRequest()
        } else {
            (activity as MainActivity?)!!.onNetworkConnectionChanged(false)
        }
        //
        return view
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private fun makeGetOrderRequest() {
        val tag_json_obj = "json_socity_req"
        val params: MutableMap<String, String?> = HashMap()
        params["d_id"] = get_id
        val jsonObjReq = CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_ORDER_URL, params, Response.Listener { response ->
            Log.d(TAG, response.toString())
            val gson = Gson()
            val listType = object : TypeToken<List<My_Pending_order_model?>?>() {}.type
            my_order_modelList = gson.fromJson(response.toString(), listType)
            val myPendingOrderAdapter = My_Pending_Order_adapter(my_order_modelList)
            rv_myorder!!.adapter = myPendingOrderAdapter
            myPendingOrderAdapter.notifyDataSetChanged()
            if (my_order_modelList.isEmpty()) {
                Toast.makeText(activity, resources.getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            VolleyLog.d(TAG, "Error: " + error.message)
            if (error is TimeoutError || error is NoConnectionError) {
                Toast.makeText(activity, resources.getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show()
            }
        })
        // Adding request to request queue
        AppController.instance!!.addToRequestQueue(jsonObjReq, tag_json_obj)
    }

    companion object {
        private val TAG = My_Pending_Order::class.java.simpleName
    }
}