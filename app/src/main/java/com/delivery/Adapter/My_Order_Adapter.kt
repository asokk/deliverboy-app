package com.delivery.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.delivery.Activity.OrderDetail
import com.delivery.Model.My_order_model
import com.delivery.R

class My_Order_Adapter : RecyclerView.Adapter<My_Order_Adapter.MyViewHolder> {
    private var modelList: List<My_order_model>
    private var inflater: LayoutInflater? = null
    private var currentFragment: Fragment? = null
    var preferences: SharedPreferences? = null
    private var context: Context? = null

    constructor(context: Context, modemodelList: List<My_order_model>, currentFragment: Fragment?) {
        this.context = context
        modelList = modemodelList
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.currentFragment = currentFragment
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_orderno: TextView
        var tv_status: TextView
        var tv_date: TextView
        var tv_time: TextView
        var tv_price: TextView
        var tv_item: TextView
        var relativetextstatus: TextView
        var tv_tracking_date: TextView
        var tv_socity: TextView
        var tv_recivername: TextView
        var tv_recivernumber: TextView
        var tv_house: TextView
        var cardView: CardView

        init {
            tv_orderno = view.findViewById<View>(R.id.tv_order_no) as TextView
            tv_status = view.findViewById<View>(R.id.tv_order_status) as TextView
            relativetextstatus = view.findViewById<View>(R.id.status) as TextView
            tv_tracking_date = view.findViewById<View>(R.id.tracking_date) as TextView
            tv_date = view.findViewById<View>(R.id.tv_order_date) as TextView
            tv_time = view.findViewById<View>(R.id.tv_order_time) as TextView
            tv_price = view.findViewById<View>(R.id.tv_order_price) as TextView
            tv_item = view.findViewById<View>(R.id.tv_order_item) as TextView
            tv_socity = view.findViewById(R.id.tv_societyname)
            tv_house = view.findViewById(R.id.tv_house)
            tv_recivername = view.findViewById(R.id.tv_recivername)
            tv_recivernumber = view.findViewById(R.id.tv_recivernmobile)
            cardView = view.findViewById(R.id.card_view)
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val saleid = modelList[position].sale_id
                    val placedon = modelList[position].on_date
                    val time = modelList[position].delivery_time_from + "-" + modelList[position].delivery_time_to
                    val item = modelList[position].total_items
                    val ammount = modelList[position].total_amount
                    val status = modelList[position].status
                    val society = modelList[position].socityname
                    val house = modelList[position].house
                    val recivername = modelList[position].recivername
                    val recivermobile = modelList[position].recivermobile
                    val intent = Intent(context, OrderDetail::class.java)
                    intent.putExtra("sale_id", saleid)
                    intent.putExtra("placedon", placedon)
                    intent.putExtra("time", time)
                    intent.putExtra("item", item)
                    intent.putExtra("ammount", ammount)
                    intent.putExtra("status", status)
                    intent.putExtra("socity_name", society)
                    intent.putExtra("house_no", house)
                    intent.putExtra("receiver_name", recivername)
                    intent.putExtra("receiver_mobile", recivermobile)
                    context!!.startActivity(intent)
                    //
                }
            }
        }
    }

    constructor(modelList: List<My_order_model>) {
        this.modelList = modelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_order_rv, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]
        try {
            holder.tv_orderno.text = mList.sale_id
            if (mList.status == "0") {
                holder.tv_status.text = context!!.resources.getString(R.string.pending)
                holder.relativetextstatus.text = context!!.resources.getString(R.string.pending)
            } else if (mList.status == "1") {
                holder.tv_status.text = context!!.resources.getString(R.string.confirm)
                holder.relativetextstatus.text = context!!.resources.getString(R.string.confirm)
                holder.tv_status.setTextColor(context!!.resources.getColor(R.color.green))
            } else if (mList.status == "2") {
                holder.tv_status.text = context!!.resources.getString(R.string.outfordeliverd)
                holder.relativetextstatus.text = context!!.resources.getString(R.string.outfordeliverd)
                holder.tv_status.setTextColor(context!!.resources.getColor(R.color.green))
            } else if (mList.status == "4") {
                holder.tv_status.text = context!!.resources.getString(R.string.delivered)
                holder.relativetextstatus.text = context!!.resources.getString(R.string.delivered)
                holder.tv_status.setTextColor(context!!.resources.getColor(R.color.green))
            }
        } catch (e: Exception) {
        }
        holder.tv_date.text = mList.on_date
        holder.tv_tracking_date.text = mList.on_date
        preferences = context!!.getSharedPreferences("lan", Context.MODE_PRIVATE)
        val language = preferences!!.getString("language", "")
        if (language.contains("spanish")) {
            var timefrom = mList.delivery_time_from
            var timeto = mList.delivery_time_to
            timefrom = timefrom!!.replace("pm", "م")
            timefrom = timefrom.replace("am", "ص")
            timeto = timeto!!.replace("pm", "م")
            timeto = timeto.replace("am", "ص")
            val time = "$timefrom-$timeto"
            holder.tv_time.text = time
        } else {
            val timefrom = mList.delivery_time_from
            val timeto = mList.delivery_time_to
            val time = "$timefrom-$timeto"
            holder.tv_time.text = time
        }
        holder.tv_price.text = mList.total_amount + context!!.resources.getString(R.string.currency)
        holder.tv_item.text = context!!.resources.getString(R.string.tv_cart_item) + mList.total_items
        holder.tv_socity.text = mList.socityname
        holder.tv_house.text = mList.house
        holder.tv_recivername.text = modelList[position].recivername
        holder.tv_recivernumber.text = mList.recivermobile
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}