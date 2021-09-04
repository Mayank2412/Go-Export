@file:Suppress("DEPRECATION")

package com.macapps.go

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat


class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.macapps.go.R.layout.activity_tutorial)
        val card= findViewById<CardView>(com.macapps.go.R.id.card1)
        val sharedPref = getSharedPreferences("key1", Context.MODE_PRIVATE)
        val animation: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            com.macapps.go.R.anim.myanimation2
        )
        card.startAnimation(animation);
        var closebtn=findViewById<Button>(com.macapps.go.R.id.closebtn)

        closebtn.setOnClickListener() {
            if (sharedPref.getBoolean("firstrun", true)){
            var permissions: Array<String> = arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(this,permissions,1)
            val i = Intent(this@TutorialActivity,PrimaryActivity::class.java)
            startActivity(i)
        }
            else{
                finish()
            }
        }
    }
}