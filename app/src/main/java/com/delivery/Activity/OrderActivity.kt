package com.delivery.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.OnTabSelectedListener
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.delivery.Adapter.PagerOrderAdapter
import com.delivery.MainActivity
import com.delivery.R
import com.franmontiel.localechanger.LocaleChanger

class OrderActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.orders)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@OrderActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        val tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.pending)))
        tabLayout.addTab(tabLayout.newTab().setText(resources.getString(R.string.pastorder)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        val adapter = PagerOrderAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        wrapTabIndicatorToTitle(tabLayout, 80, 80)
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun setTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val intent = Intent(this@OrderActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun wrapTabIndicatorToTitle(tabLayout: TabLayout, externalMargin: Int, internalMargin: Int) {
        val tabStrip = tabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val childCount = tabStrip.childCount
            for (i in 0 until childCount) {
                val tabView = tabStrip.getChildAt(i)
                tabView.minimumWidth = 0
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                if (tabView.layoutParams is MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as MarginLayoutParams
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin)
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin)
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin)
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }

    private fun setMargin(layoutParams: MarginLayoutParams, start: Int, end: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginStart = start
            layoutParams.marginEnd = end
        } else {
            layoutParams.leftMargin = start
            layoutParams.rightMargin = end
        }
    }
}