package com.delivery

import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.delivery.Config.BaseURL
import com.delivery.Fonts.CustomTypefaceSpan
import com.delivery.Fragment.Home
import com.delivery.MainActivity
import com.delivery.NetworkConnectivity.NoInternetConnection
import com.delivery.util.ConnectivityReceiver.ConnectivityReceiverListener
import com.delivery.util.Session_management
import com.franmontiel.localechanger.LocaleChanger
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiverListener {
    private var iv_profile: ImageView? = null
    private var nav_menu: Menu? = null
    private var tv_name: TextView? = null
    var imageView: ImageView? = null
    var mTitle: TextView? = null
    var toolbar: Toolbar? = null
    var padding = 0
    private val bitmap: Bitmap? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    private var sessionManagement: Session_management? = null
    override fun attachBaseContext(newBase: Context) {
        var newBase: Context? = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        sharedPreferences = getSharedPreferences("lan", Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        for (i in 0 until toolbar!!.childCount) {
            val view = toolbar!!.getChildAt(i)
            if (view is TextView) {
                val myCustomFont = Typeface.createFromAsset(assets, "Font/Bold.otf")
                view.typeface = myCustomFont
            }
        }
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        supportActionBar!!.setTitle(resources.getString(R.string.app_name))
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val m = navigationView.menu
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)
            val subMenu = mi.subMenu
            if (subMenu != null && subMenu.size() > 0) {
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)
                    applyFontToMenuItem(subMenuItem)
                }
            }
            applyFontToMenuItem(mi)
        }
        sessionManagement = Session_management(this@MainActivity)
        val headerView = navigationView.getHeaderView(0)
        navigationView.background.setColorFilter(-0x80000000, PorterDuff.Mode.MULTIPLY)
        navigationView.setNavigationItemSelectedListener(this)
        nav_menu = navigationView.menu
        val header = (findViewById<View>(R.id.nav_view) as NavigationView).getHeaderView(0)
        iv_profile = header.findViewById<View>(R.id.iv_header_img) as ImageView
        tv_name = header.findViewById<View>(R.id.tv_header_name) as TextView
        updateHeader()
        sideMenu()
        if (savedInstanceState == null) {
            val fm: Fragment = Home()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        }
        fragmentManager.addOnBackStackChangedListener {
            try {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                val fr = fragmentManager.findFragmentById(R.id.contentPanel)
                val fm_name = fr.javaClass.simpleName
                Log.e("backstack: ", ": $fm_name")
                if (fm_name.contentEquals("Home_fragment")) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    toggle.isDrawerIndicatorEnabled = true
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    toggle.syncState()
                } else if (fm_name.contentEquals("My_order_fragment") ||
                        fm_name.contentEquals("Thanks_fragment")) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toggle.isDrawerIndicatorEnabled = false
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    toggle.syncState()
                    toggle.toolbarNavigationClickListener = View.OnClickListener {
                        val fm: Fragment = Home()
                        val fragmentManager = fragmentManager
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit()
                    }
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    toggle.isDrawerIndicatorEnabled = false
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    toggle.syncState()
                    toggle.toolbarNavigationClickListener = View.OnClickListener { onBackPressed() }
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
    }

    fun updateHeader() {
        if (sessionManagement!!.isLoggedIn) {
            val getname = sessionManagement!!.userDetails[BaseURL.KEY_NAME]
            tv_name!!.text = getname
        }
    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "Font/Bold.otf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(CustomTypefaceSpan("", font), 0, mNewTitle.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        mi.title = mNewTitle
    }

    fun sideMenu() {
        if (sessionManagement!!.isLoggedIn) {
            nav_menu!!.findItem(R.id.nav_log_out).isVisible = true
        } else { //            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu!!.findItem(R.id.nav_log_out).isVisible = false
        }
    }

    fun setTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val fm: Fragment? = null
        val args = Bundle()
        if (id == R.id.nav_order) {
            val fma: Fragment = Home()
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fma, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
        } else if (id == R.id.nav_log_out) {
            sessionManagement!!.logoutSession()
            finish()
        }
        if (fm != null) {
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit()
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }

    private fun showSnack(isConnected: Boolean) {
        if (!isConnected) {
            val intent = Intent(this@MainActivity, NoInternetConnection::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.language, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_language) {
            openLanguageDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openLanguageDialog() {
        val v = LayoutInflater.from(this).inflate(R.layout.dialog_language, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(v)
        val lEnglish = v.findViewById<TextView>(R.id.l_english)
        val lSpanish = v.findViewById<TextView>(R.id.l_arabic)
        val dialog = builder.create()
        lEnglish.setOnClickListener {
            LocaleChanger.setLocale(Locale.ENGLISH)
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            editor!!.putString("language", "english")
            editor!!.apply()
            recreate()
            dialog.dismiss()
        }
        lSpanish.setOnClickListener {
            LocaleChanger.setLocale(Locale("ar", "ARABIC"))
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
            editor!!.putString("language", "spanish")
            editor!!.apply()
            recreate()
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}