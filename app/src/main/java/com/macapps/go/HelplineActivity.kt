package com.macapps.go

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat

class HelplineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpline)
        var backbtn: Button=findViewById(R.id.backbutton5)
        backbtn.setOnClickListener(){
            finish()
        }
        var nennum=findViewById<Button>(R.id.nennum)
        nennum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:112"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var policenum=findViewById<Button>(R.id.policenum)
        policenum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:100"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var firenum=findViewById<Button>(R.id.firenum)
        firenum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:101"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var ambnum=findViewById<Button>(R.id.ambnum)
        ambnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:102"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var whnum=findViewById<Button>(R.id.whnum)
        whnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:1091"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var whdnum=findViewById<Button>(R.id.whdnum)
        whdnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:181"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var mwcnum=findViewById<Button>(R.id.mwcnum)
        mwcnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:1094"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var cdsnum=findViewById<Button>(R.id.cdsnum)
        cdsnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:1098"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
        var mhnum=findViewById<Button>(R.id.mhnum)
        mhnum.setOnClickListener(){
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:18005990019"))
            var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                startActivity(surf)
            }
        }
    }
}