package com.macapps.go

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class DirectoryActivity : AppCompatActivity() {
    var phonenumber1: String?= null
    var phonenumber2: String?= null
    var phonenumber3: String?= null
    var phonenumber4: String?= null
    var phonenumber5: String?= null
    var phonenumber6: String?= null
    var phonenumber7: String?= null
    var phonenumber8: String?= null
    var phonenumber9: String?= null
    var phonenumber10: String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var backbtn: Button =findViewById(R.id.backbutton4)
        backbtn.setOnClickListener(){
            finish()
        }
        val sharedPref = this.getSharedPreferences("key2", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        phonenumber1=prefs.getString("phonenumber1", "")
        phonenumber2=prefs.getString("phonenumber2", "")
        phonenumber3=prefs.getString("phonenumber3", "")
        phonenumber4=prefs.getString("phonenumber4", "")
        phonenumber5=prefs.getString("phonenumber5", "")
        phonenumber6=prefs.getString("phonenumber6", "")
        phonenumber7=prefs.getString("phonenumber7", "")
        phonenumber8=prefs.getString("phonenumber8", "")
        phonenumber9=prefs.getString("phonenumber9", "")
        phonenumber10=prefs.getString("phonenumber10", "")
        with(sharedPref.edit()) {
            putString("phonenumber1", phonenumber1)
            putString("phonenumber2", phonenumber2)
            putString("phonenumber3", phonenumber3)
            putString("phonenumber4", phonenumber4)
            putString("phonenumber5", phonenumber5)
            putString("phonenumber6", phonenumber6)
            putString("phonenumber7", phonenumber7)
            putString("phonenumber8", phonenumber8)
            putString("phonenumber9", phonenumber9)
            putString("phonenumber10", phonenumber10)
            commit()
            apply()
        }

    }

    override fun onResume() {
        super.onResume()
        val sharedPref = this.getSharedPreferences("key2", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        phonenumber1=prefs.getString("phonenumber1", "")
        phonenumber2=prefs.getString("phonenumber2", "")
        phonenumber3=prefs.getString("phonenumber3", "")
        phonenumber4=prefs.getString("phonenumber4", "")
        phonenumber5=prefs.getString("phonenumber5", "")
        phonenumber6=prefs.getString("phonenumber6", "")
        phonenumber7=prefs.getString("phonenumber7", "")
        phonenumber8=prefs.getString("phonenumber8", "")
        phonenumber9=prefs.getString("phonenumber9", "")
        phonenumber10=prefs.getString("phonenumber10", "")
        with(sharedPref.edit()) {
            putString("phonenumber1", phonenumber1)
            putString("phonenumber2", phonenumber2)
            putString("phonenumber3", phonenumber3)
            putString("phonenumber4", phonenumber4)
            putString("phonenumber5", phonenumber5)
            putString("phonenumber6", phonenumber6)
            putString("phonenumber7", phonenumber7)
            putString("phonenumber8", phonenumber8)
            putString("phonenumber9", phonenumber9)
            putString("phonenumber10", phonenumber10)
            commit()
            apply()
        }
    }
    override fun onPause() {
        super.onPause()
        val sharedPref = this.getSharedPreferences("key2", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        phonenumber1=prefs.getString("phonenumber1", "")
        phonenumber2=prefs.getString("phonenumber2", "")
        phonenumber3=prefs.getString("phonenumber3", "")
        phonenumber4=prefs.getString("phonenumber4", "")
        phonenumber5=prefs.getString("phonenumber5", "")
        phonenumber6=prefs.getString("phonenumber6", "")
        phonenumber7=prefs.getString("phonenumber7", "")
        phonenumber8=prefs.getString("phonenumber8", "")
        phonenumber9=prefs.getString("phonenumber9", "")
        phonenumber10=prefs.getString("phonenumber10", "")
        with(sharedPref.edit()) {
            putString("phonenumber1", phonenumber1)
            putString("phonenumber2", phonenumber2)
            putString("phonenumber3", phonenumber3)
            putString("phonenumber4", phonenumber4)
            putString("phonenumber5", phonenumber5)
            putString("phonenumber6", phonenumber6)
            putString("phonenumber7", phonenumber7)
            putString("phonenumber8", phonenumber8)
            putString("phonenumber9", phonenumber9)
            putString("phonenumber10", phonenumber10)
            commit()
            apply()
        }
    }
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.directory_preferences, rootKey)
        }
    }
}