package com.delivery.Fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.*
import com.delivery.Adapter.My_Order_Adapter
import com.delivery.AppController
import com.delivery.Config.BaseURL
import com.delivery.Model.My_order_model
import com.delivery.R
import com.delivery.util.CustomVolleyJsonArrayRequest
import com.delivery.util.Session_management
import org.json.JSONException
import java.util.*

class Home : Fragment() {
    private var rv_myorder: RecyclerView? = null
    private val my_order_modelList: MutableList<My_order_model> = ArrayList()
    private var sessionManagement: Session_management? = null
    var get_id: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        val view = inflater.inflate(R.layout.fragment_home,
                container, false)
        sessionManagement = Session_management(activity)
        get_id = sessionManagement!!.userDetails[BaseURL.KEY_NAME]
        rv_myorder = view.findViewById<View>(R.id.rv_myorder) as RecyclerView
        rv_myorder!!.layoutManager = LinearLayoutManager(activity)
        makeGetOrderRequest()
        return view
    }

    private fun makeGetOrderRequest() {
        val tag_json_obj = "json_socity_req"
        val params: MutableMap<String, String?> = HashMap()
        params["d_id"] = get_id
        val jsonObjReq = CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_ORDER_URL, params, Response.Listener { response ->
            try {
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val saleid = obj.getString("sale_id")
                    val placedon = obj.getString("on_date")
                    val timefrom = obj.getString("delivery_time_from")
                    val timeto = obj.getString("delivery_time_from")
                    val item = obj.getString("total_items")
                    val ammount = obj.getString("total_amount")
                    val status = obj.getString("status")
                    val society = obj.getString("socity_name")
                    val house = obj.getString("house_no")
                    val rename = obj.getString("receiver_name")
                    val renumber = obj.getString("receiver_mobile")
                    val my_order_model = My_order_model()
                    my_order_model.socityname = society
                    my_order_model.house = house
                    my_order_model.recivername = rename
                    my_order_model.recivermobile = renumber
                    my_order_model.delivery_time_from = timefrom
                    my_order_model.sale_id = saleid
                    my_order_model.on_date = placedon
                    my_order_model.delivery_time_to = timeto
                    my_order_model.total_amount = ammount
                    my_order_model.status = status
                    my_order_model.total_items = item
                    my_order_modelList.add(my_order_model)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val myPendingOrderAdapter = My_Order_Adapter(my_order_modelList)
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
        AppController.instance!!.addToRequestQueue(jsonObjReq, tag_json_obj)
    }

    companion object {
        private val TAG = Home::class.java.simpleName
    }
}