package com.delivery.Adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.delivery.Model.My_Past_order_model
import com.delivery.R

class My_Past_Order_adapter : RecyclerView.Adapter<My_Past_Order_adapter.MyViewHolder> {
    private var modelList: List<My_Past_order_model>
    private var inflater: LayoutInflater? = null
    private var currentFragment: Fragment? = null
    private var context: Context? = null

    constructor(context: Context, modemodelList: List<My_Past_order_model>, currentFragment: Fragment?) {
        this.context = context
        this.modelList = modemodelList
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
        var tv_pending_date: TextView? = null
        var tv_pending_time: TextView? = null
        var tv_confirm_date: TextView? = null
        var tv_confirm_time: TextView? = null
        var tv_delevered_date: TextView? = null
        var tv_delevered_time: TextView? = null
        var tv_cancel_date: TextView? = null
        var tv_cancel_time: TextView? = null
        var view1: View? = null
        var view2: View? = null
        var view3: View? = null
        var view4: View? = null
        var view5: View? = null
        var view6: View? = null
        var relative_background: RelativeLayout? = null
        var Confirm: ImageView? = null
        var Out_For_Deliverde: ImageView? = null
        var Delivered: ImageView? = null
        var cardView: CardView
        var tv_methid1: TextView? = null
        var method: String? = null

        init {
            tv_orderno = view.findViewById<View>(R.id.tv_order_no) as TextView
            tv_status = view.findViewById<View>(R.id.tv_order_status) as TextView
            relativetextstatus = view.findViewById<View>(R.id.status) as TextView
            tv_tracking_date = view.findViewById<View>(R.id.tracking_date) as TextView
            tv_date = view.findViewById<View>(R.id.tv_order_date) as TextView
            tv_time = view.findViewById<View>(R.id.tv_order_time) as TextView
            tv_price = view.findViewById<View>(R.id.tv_order_price) as TextView
            tv_item = view.findViewById<View>(R.id.tv_order_item) as TextView
            cardView = view.findViewById(R.id.card_view)
        }
    }

    constructor(modelList: List<My_Past_order_model>) {
        this.modelList = modelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_my_past_order_rv, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]
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
        holder.tv_orderno.text = mList.sale_id
        holder.tv_date.text = mList.on_date
        holder.tv_tracking_date.text = mList.on_date
        holder.tv_time.text = mList.delivery_time_from + "-" + mList.delivery_time_to
        holder.tv_price.text = context!!.resources.getString(R.string.currency) + mList.total_amount
        holder.tv_item.text = context!!.resources.getString(R.string.tv_cart_item) + mList.total_items
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}