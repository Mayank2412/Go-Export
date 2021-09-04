@file:Suppress("DEPRECATION")

package com.macapps.go

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat


class TipsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tips)
        var backbtn: Button =findViewById(R.id.backbutton7)
        backbtn.setOnClickListener(){
            finish()
        }
    }
}