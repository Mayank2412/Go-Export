@file:Suppress("DEPRECATION")

package com.macapps.go

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView



class FeaturesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.macapps.go.R.layout.activity_features)
        val card= findViewById<CardView>(com.macapps.go.R.id.card2)

        val animation: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            com.macapps.go.R.anim.myanimation2
        )
        card.startAnimation(animation);
        var closebtn=findViewById<Button>(com.macapps.go.R.id.closebtn2)

        closebtn.setOnClickListener() {
            finish()
        }
    }
}