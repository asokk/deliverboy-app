package com.delivery.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.delivery.Fragment.My_Past_Order
import com.delivery.Fragment.My_Pending_Order

class PagerOrderAdapter(fm: FragmentManager?, var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                My_Pending_Order()
            }
            1 -> {
                My_Past_Order()
            }
            else ->  My_Pending_Order()
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }

}