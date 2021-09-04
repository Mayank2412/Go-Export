package com.macapps.go

import android.Manifest
import android.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.macapps.go.R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(com.macapps.go.R.id.settings, SettingsFragment())
                .commit()
        }
        val sharedPref = this.getSharedPreferences("key1", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var backbtn: Button =findViewById(com.macapps.go.R.id.backbutton8)
        backbtn.setOnClickListener(){
            finish()
        }



// region variables


        var address: String?= null

        var hidefolder: String?= null
        var phonelock: Boolean?=null
        var sendlocation: Boolean?=null
        var autodial: Boolean?=null
        var applock: Boolean?=null
        address=prefs.getString("address", "")
        hidefolder=prefs.getString("hidefolder", "")
        phonelock=prefs.getBoolean("phonelock",true)
        sendlocation=prefs.getBoolean("sendlocation",true)
        autodial=prefs.getBoolean("autodial",true)
        applock=prefs.getBoolean("applock",false)
//endregion

// region about


//endregion
        with(sharedPref.edit()) {
            putString("address", address)
            putString("hidefolder", hidefolder)
            putBoolean("phonelock",phonelock)
            putBoolean("sendlocation",sendlocation)
            putBoolean("autodial",autodial)
            putBoolean("applock",applock)
            commit()
            apply()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume()    {
        super.onResume()
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var address: String?= null
        var hidefolder: String?= null
        var phonelock: Boolean?=null
        var sendlocation: Boolean?=null
        var autodial: Boolean?=null
        var applock: Boolean?=null
        address=prefs.getString("address", "")
        hidefolder=prefs.getString("hidefolder", "")
        phonelock=prefs.getBoolean("phonelock",true)
        sendlocation=prefs.getBoolean("sendlocation",true)
        autodial=prefs.getBoolean("autodial",true)
        applock=prefs.getBoolean("applock",false)
        var sharedPref = this.getSharedPreferences("key1", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("address", address)
            putString("hidefolder", hidefolder)
            putBoolean("phonelock",phonelock)
            putBoolean("sendlocation",sendlocation)
            putBoolean("autodial",autodial)
            putBoolean("applock",applock)
            commit()
            apply()
        }
    }
    override fun onPause()    {
        super.onPause()
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var address: String?= null
        var hidefolder: String?= null
        var phonelock: Boolean?=null
        var sendlocation: Boolean?=null
        var autodial: Boolean?=null
        var applock: Boolean?=null
        address=prefs.getString("address", "")
        hidefolder=prefs.getString("hidefolder", "")
        phonelock=prefs.getBoolean("phonelock",true)
        sendlocation=prefs.getBoolean("sendlocation",true)
        autodial=prefs.getBoolean("autodial",true)
        applock=prefs.getBoolean("applock",false)
        var sharedPref = this.getSharedPreferences("key1", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("address", address)
            putString("hidefolder", hidefolder)
            putBoolean("phonelock",phonelock)
            putBoolean("sendlocation",sendlocation)
            putBoolean("autodial",autodial)
            putBoolean("applock",applock)

            commit()
            apply()
        }




                var permissions: Array<String> = arrayOf(
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO)
                ActivityCompat.requestPermissions(this@SettingsActivity,permissions,1)




    }
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"Please restart the app for changes to take place.",Toast.LENGTH_SHORT).show()
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(com.macapps.go.R.xml.root_preferences, rootKey)
        }


    }

}