package com.delivery.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.delivery.Adapter.My_order_detail_adapter
import com.delivery.AppController
import com.delivery.Config.BaseURL
import com.delivery.MainActivity
import com.delivery.Model.My_order_detail_model
import com.delivery.R
import com.delivery.util.ConnectivityReceiver
import com.delivery.util.CustomVolleyJsonArrayRequest
import com.franmontiel.localechanger.LocaleChanger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class OrderDetail : AppCompatActivity() {
    var tv_orderno: TextView? = null
    var tv_status: TextView? = null
    var tv_date: TextView? = null
    var tv_time: TextView? = null
    var tv_price: TextView? = null
    var tv_item: TextView? = null
    var relativetextstatus: TextView? = null
    var tv_tracking_date: TextView? = null
    private var sale_id: String? = null
    var Mark_Delivered: RelativeLayout? = null
    private var rv_detail_order: RecyclerView? = null
    private var my_order_detail_modelList: List<My_order_detail_model> = ArrayList()
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(resources.getString(R.string.orderdetail))
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@OrderDetail, MainActivity::class.java)
            startActivity(intent)
        }
        rv_detail_order = findViewById<View>(R.id.product_recycler) as RecyclerView
        rv_detail_order!!.layoutManager = LinearLayoutManager(applicationContext)
        rv_detail_order!!.addItemDecoration(DividerItemDecoration(applicationContext, 0))
        tv_orderno = findViewById<View>(R.id.tv_order_no) as TextView
        tv_status = findViewById<View>(R.id.tv_order_status) as TextView
        relativetextstatus = findViewById<View>(R.id.status) as TextView
        tv_tracking_date = findViewById<View>(R.id.tracking_date) as TextView
        tv_date = findViewById<View>(R.id.tv_order_date) as TextView
        tv_time = findViewById<View>(R.id.tv_order_time) as TextView
        tv_price = findViewById<View>(R.id.tv_order_price) as TextView
        tv_item = findViewById<View>(R.id.tv_order_item) as TextView
        sale_id = intent.getStringExtra("sale_id")
        if (ConnectivityReceiver.isConnected) {
            makeGetOrderDetailRequest(sale_id)
        } else {
            Toast.makeText(applicationContext, "Network Issue", Toast.LENGTH_SHORT).show()
        }
        val placed_on = intent.getStringExtra("placedon")
        val time = intent.getStringExtra("time")
        val item = intent.getStringExtra("item")
        val amount = intent.getStringExtra("ammount")
        val stats = intent.getStringExtra("status")
        Mark_Delivered = findViewById<View>(R.id.btn_mark_delivered) as RelativeLayout
        Mark_Delivered!!.setOnClickListener {
            val intent = Intent(this@OrderDetail, GetSignature::class.java)
            intent.putExtra("sale", sale_id)
            startActivity(intent)
        }
        if (stats == "0") {
            tv_status!!.text = resources.getString(R.string.pending)
            relativetextstatus!!.text = resources.getString(R.string.pending)
        } else if (stats == "1") {
            tv_status!!.text = resources.getString(R.string.confirm)
            relativetextstatus!!.text = resources.getString(R.string.confirm)
        } else if (stats == "2") {
            tv_status!!.text = resources.getString(R.string.outfordeliverd)
            relativetextstatus!!.text = resources.getString(R.string.outfordeliverd)
        } else if (stats == "4") {
            tv_status!!.text = resources.getString(R.string.delivered)
            Mark_Delivered!!.visibility = View.GONE
            relativetextstatus!!.text = resources.getString(R.string.delivered)
        }
        tv_orderno!!.text = sale_id
        tv_date!!.text = placed_on
        tv_time!!.text = time
        tv_item!!.text = item
        tv_tracking_date!!.text = placed_on
        tv_price!!.text = resources.getString(R.string.currency) + amount
    }

    private fun makeGetOrderDetailRequest(sale_id: String?) {
        val tag_json_obj = "json_order_detail_req"
        val params: MutableMap<String, String?> = HashMap()
        params["sale_id"] = sale_id
        val jsonObjReq = CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.OrderDetail, params, Response.Listener { response ->
            val gson = Gson()
            val listType = object : TypeToken<List<My_order_detail_model?>?>() {}.type
            my_order_detail_modelList = gson.fromJson(response.toString(), listType)
            val adapter = My_order_detail_adapter(my_order_detail_modelList)
            rv_detail_order!!.adapter = adapter
            adapter.notifyDataSetChanged()
            if (my_order_detail_modelList.isEmpty()) {
                Toast.makeText(this@OrderDetail, resources.getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            // VolleyLog.d(TAG, "Error: " + error.getMessage());
            if (error is TimeoutError || error is NoConnectionError) {
                Toast.makeText(this@OrderDetail, resources.getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show()
            }
        })
        // Adding request to request queue
        AppController.instance!!.addToRequestQueue(jsonObjReq, tag_json_obj)
    }
}