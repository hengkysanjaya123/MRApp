package com.example.myapplication

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.app_bar_admin.*

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var fragmentClass: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.masterUser)
        loadFragment(MasterUser::class.java)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.addNewUser -> {


                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.


        when (item.itemId) {
            R.id.masterUser -> {
                // Handle the camera action
                fragmentClass = MasterUser::class.java
            }
            R.id.viewReferral -> {
                fragmentClass = ViewReferral::class.java
            }
            R.id.logout -> {
                this.finish()
                Toast.makeText(this, "See you !", Toast.LENGTH_SHORT).show()
            }
        }

        loadFragment(fragmentClass)



        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        loadFragment(fragmentClass)
    }

    private fun loadFragment(fragmentClass: Class<*>?) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass?.newInstance() as Fragment
            var fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment!!).commit()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
