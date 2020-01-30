package com.delivery.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import android.widget.Toast
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.delivery.Adapter.My_Past_Order_adapter
import com.delivery.AppController
import com.delivery.Config.BaseURL
import com.delivery.MainActivity
import com.delivery.Model.My_Past_order_model
import com.delivery.R
import com.delivery.util.ConnectivityReceiver
import com.delivery.util.CustomVolleyJsonArrayRequest
import com.delivery.util.Session_management
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class My_Past_Order : Fragment() {
    private var rv_myorder: RecyclerView? = null
    private var my_order_modelList: List<My_Past_order_model> = ArrayList()
    var tHost: TabHost? = null
    private var sessionManagement: Session_management? = null
    var get_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_past_order, container, false)
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

        return view
    }

    private fun makeGetOrderRequest() {
        val tag_json_obj = "json_socity_req"
        val params: MutableMap<String, String?> = HashMap()
        params["d_id"] = get_id
        val jsonObjReq = CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_DELIVERD_ORDER_URL, params, Response.Listener { response ->
            val gson = Gson()
            val listType = object : TypeToken<List<My_Past_order_model?>?>() {}.type
            my_order_modelList = gson.fromJson(response.toString(), listType)
            val adapter = My_Past_Order_adapter(my_order_modelList)
            rv_myorder!!.adapter = adapter
            adapter.notifyDataSetChanged()
            if (my_order_modelList.isEmpty()) { // Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
            }
        }, Response.ErrorListener { error ->
            if (error is TimeoutError || error is NoConnectionError) {
                Toast.makeText(activity, resources.getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show()
            }
        })
        AppController.instance!!.addToRequestQueue(jsonObjReq, tag_json_obj)
    }
}