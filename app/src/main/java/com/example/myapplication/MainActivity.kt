package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed(timerTask {
            val intt = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intt)
        }, 1500)

    }

    override fun onResume() {
        super.onResume()

//        Handler().postDelayed(timerTask {
//            val intt = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intt)
//        }, 1500)
    }
}
