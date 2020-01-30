package com.delivery.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.delivery.Config.BaseURL
import com.delivery.Model.My_order_detail_model
import com.delivery.R

class My_order_detail_adapter(private val modelList: List<My_order_detail_model>) : RecyclerView.Adapter<My_order_detail_adapter.MyViewHolder>() {
    private var context: Context? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_title: TextView
        var tv_price: TextView
        var tv_qty: TextView
        var iv_img: ImageView

        init {
            tv_title = view.findViewById<View>(R.id.tv_order_Detail_title) as TextView
            tv_price = view.findViewById<View>(R.id.tv_order_Detail_price) as TextView
            tv_qty = view.findViewById<View>(R.id.tv_order_Detail_qty) as TextView
            iv_img = view.findViewById<View>(R.id.iv_order_detail_img) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_my_order_detail_rv, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.product_image)
                .centerCrop()
                .placeholder(R.drawable.newdownload)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_img)
        holder.tv_title.text = mList.product_name
        holder.tv_price.text = mList.price
        holder.tv_qty.text = mList.qty
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}