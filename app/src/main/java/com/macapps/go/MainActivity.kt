@file:Suppress("DEPRECATION")

package com.macapps.go

import android.Manifest
import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


var SPLASH_SCREEN_TIME_OUT: Long = 2000
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.macapps.go.R.layout.activity_main)
        val sharedPref = getSharedPreferences("key1", Context.MODE_PRIVATE)
        val image= findViewById<ImageView>(com.macapps.go.R.id.logoborder)
        val animation: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            com.macapps.go.R.anim.myanimation
        )
        image.startAnimation(animation);
        Handler().postDelayed(Runnable {
            if (sharedPref.getBoolean("firstrun", true)) {
                val i = Intent(this@MainActivity,TutorialActivity::class.java)
                startActivity(i)
                sharedPref.edit().putBoolean("firstrun", false).commit();
            }
            else{
                val i = Intent(this@MainActivity,PrimaryActivity::class.java)
                startActivity(i)
            }

            finish()
        }, SPLASH_SCREEN_TIME_OUT)
    }
}