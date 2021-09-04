package com.macapps.go

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        var backbtn: Button =findViewById(R.id.backbutton3)
        backbtn.setOnClickListener(){
            finish()
        }
    }
}