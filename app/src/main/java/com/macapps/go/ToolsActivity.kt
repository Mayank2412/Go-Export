package com.macapps.go

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
import android.media.SoundPool
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class ToolsActivity : AppCompatActivity() {
    companion object {
        const val RESULT_ENABLE = 1
    }
    // region variables
    lateinit var mAdminComponentName: ComponentName
    lateinit var mDevicePolicyManager: DevicePolicyManager
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val soundPool= SoundPool(1, AudioManager.STREAM_ALARM, 0)
    var deviceManger: DevicePolicyManager? = null
    var compName: ComponentName? = null
    private val PERMISSION_ID = 42
    private val CAMERA_REQUEST = 123
    var hasCameraFlash = false
    var lat=0.0000
    var long=0.0000
    var address: String? = null
    private var phonenumber1: String?= null
    private var phonenumber2: String?= null
    private var phonenumber3: String?= null
    private var phonenumber4: String?= null
    private var phonenumber5: String?= null
    private var phonenumber6: String?= null
    private var phonenumber7: String?= null
    private var phonenumber8: String?= null
    private var phonenumber9: String?= null
    private var phonenumber10: String?= null
    var hidefolder: String?= null
    var connectivity: ConnectivityManager? = null
    var clickcount=0
    var phonelock: Boolean?=null
    var sendlocation: Boolean?=null
    var autodial: Boolean?=null
    var batteryPct: Double=0.00
    var capacity: Double=0.00
    var counte=0
    var check=0
    var soundId=1
    var countflash=0
//endregion

    /*-------------END OF VARIABLE DECLARATION------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

// region PRE-FUNCTIONS 1
        mAdminComponentName = DeviceAdmin.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
  //      mDevicePolicyManager.removeActiveAdmin(mAdminComponentName)
        connectivity= getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val sharedPref = getSharedPreferences("key1", Context.MODE_PRIVATE)
        address=sharedPref.getString("address", "")
        hidefolder=sharedPref.getString("hidefolder", "")
        phonelock=sharedPref.getBoolean("phonelock", true)
        sendlocation=sharedPref.getBoolean("sendlocation", true)
        autodial=sharedPref.getBoolean("autodial", true)
        var audioManager= getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE,0)
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),0)
        soundPool.load(baseContext, R.raw.siren, 1)
//endregion
// region PRE-FUNCTIONS-2
        val newPref = getSharedPreferences("key2", Context.MODE_PRIVATE)
        phonenumber1=newPref.getString("phonenumber1", "")
        phonenumber2=newPref.getString("phonenumber2", "")
        phonenumber3=newPref.getString("phonenumber3", "")
        phonenumber4=newPref.getString("phonenumber4", "")
        phonenumber5=newPref.getString("phonenumber5", "")
        phonenumber6=newPref.getString("phonenumber6", "")
        phonenumber7=newPref.getString("phonenumber7", "")
        phonenumber8=newPref.getString("phonenumber8", "")
        phonenumber9=newPref.getString("phonenumber9", "")
        phonenumber10=newPref.getString("phonenumber10", "")
        var bm: BatteryManager =getSystemService(Service.BATTERY_SERVICE) as BatteryManager
        batteryPct = bm.getIntProperty (BatteryManager.BATTERY_PROPERTY_CAPACITY).toDouble()
        capacity=getBatteryCapacity()
        deviceManger = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManger!!.isAdminActive(compName!!)
        if (!active) {
            enablePhone()
        }
        hasCameraFlash = getPackageManager().
        hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//endregion

// region OTHER FUNCTIONS
        val settingsbtn=findViewById<Button>(R.id.settingsbutton)
        settingsbtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val helplinebtn=findViewById<Button>(R.id.toolshelplinebutton)
        helplinebtn.setOnClickListener(){
            var intent= Intent(this,HelplineActivity::class.java)
            startActivity(intent)
        }

        val smsbutton=findViewById<Button>(R.id.toolssmsbutton)
        smsbutton.setOnClickListener {
            sendURL()
        }

        val voicerecbutton=findViewById<Button>(R.id.toolsvoicerecbutton)
        voicerecbutton.setOnClickListener {
            val intent = Intent(this, VoiceActivity::class.java)
            startActivity(intent)
        }

        val lockbtn=findViewById<Button>(R.id.toolsphonelockbutton)
        lockbtn.setOnClickListener {
            lockdevice()
        }

        val locationbtn=findViewById<Button>(R.id.toolslocationbutton)
        locationbtn.setOnClickListener {

            val uriadd = Uri.parse(address)
            val uri =
                "https://www.google.com/maps/dir/?api=1&destination=$uriadd&travelmode=walking"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        val wayfinderbtn=findViewById<Button>(R.id.toolswayfinderbutton)
        wayfinderbtn.setOnClickListener {
            val intent= Intent(this,WayfinderActivity::class.java)
            startActivity(intent)
        }

        val callbtn=findViewById<Button>(R.id.toolscallbutton)
        callbtn.setOnClickListener {
            makeCalls()
        }

        val flashbtn=findViewById<Button>(R.id.toolsflashbutton)
        flashbtn.setOnClickListener {
            countflash++
            if(countflash==1) {
                val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
                val myString = "0101010101010101010101010101010101010101"
                val blinkDelay: Long = 50 //Delay in ms
                for (i in 0..50) {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, true)
                    Thread.sleep(blinkDelay)
                    cameraManager.setTorchMode(cameraId, false)
                    Thread.sleep(blinkDelay)
                }
            }
            else{
                flashLightOff()
            }
            countflash%=2
        }

        val alertbtn=findViewById<Button>(R.id.toolsalertbutton)
        alertbtn.setOnClickListener {
            if(counte==0){
                alertbtn.setBackgroundColor(Color.parseColor("#F44336"))
                playSound()
                check=1
                counte++
            }
            else{
                alertbtn.setBackgroundColor(Color.parseColor("#018786"))
                stopSound()
                counte++
            }
            counte%=2
        }

        val tutorialbtn=findViewById<Button>(R.id.toolstutorialbutton)
        tutorialbtn.setOnClickListener {
            val intent= Intent(this,TutorialActivity::class.java)
            startActivity(intent)
        }

        val tipsbtn=findViewById<Button>(R.id.toolstipsbutton)
        tipsbtn.setOnClickListener {
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)
        }

        var backbtn: Button=findViewById(R.id.backbutton2)
        backbtn.setOnClickListener(){
            finish()
        }

//endregion

    }
    // region FUNCTIONS
// region SIREN FUNCTIONS
    fun playSound(){

        if(check==0) {
            soundPool.play(soundId, 1F, 1F, 1, -1, 1F)
        }
        else{
            soundPool.resume(soundId)
        }
    }
    fun stopSound(){
        soundPool.pause(soundId)
        soundPool.unload(soundId)
    }

    //endregion
// region BATTERY FUNCTIONS
    fun getBatteryCapacity(): Double {
        var mPowerProfile_: Any? = null
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"
        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java).newInstance(this)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        var batteryCapacity: Double=0.0
        try {
            batteryCapacity = Class.forName(POWER_PROFILE_CLASS)
                .getMethod("getAveragePower", String::class.java)
                .invoke(mPowerProfile_, "battery.capacity") as Double

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return batteryCapacity
    }
    //endregion
// region LOCATION FUNCTIONS
    private fun getLastLocation() {
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude
            long = mLastLocation.longitude
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@ToolsActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    //endregion
// region SMS FUNCTIONS
    private fun sendURL(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        lat = location.latitude
                        long = location.longitude

                        val uri ="http://maps.google.com/maps?daddr=$lat,$long"
                        var capa=capacity*0.01*batteryPct
                        val message =
                            "Hey there! I need help!\nThis is my current location: $uri \nMy Battery Percentage is: $batteryPct%($capa mAh)"
                        var permissions: Array<String> = arrayOf(Manifest.permission.SEND_SMS)
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,permissions,1)
                        }
                        else {
                            try {
                                val smsManager: SmsManager = SmsManager.getDefault()
                                //  smsManager.sendTextMessage("0$phonenumber1", null, message, null, null)
                                if(phonenumber1==""&&phonenumber2==""&&phonenumber3==""&&phonenumber4==""&&phonenumber5==""&&phonenumber6==""&&phonenumber7==""&&phonenumber8==""&&phonenumber9==""&&phonenumber10==""){
                                    Toast.makeText(this,"Enter atleast one phone number to send a text message.",
                                        Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    if(phonenumber1!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber1",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber2!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber2",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber3!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber3",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber4!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber4",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber5!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber5",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber6!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber6",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber7!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber7",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber8!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber8",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber9!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber9",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    if(phonenumber10!="")
                                        smsManager.sendTextMessage(
                                            "0$phonenumber10",
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    Toast.makeText(
                                        applicationContext,
                                        "Message Sent",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(applicationContext, "Some fields are Empty", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    //endregion
// region LOCK PHONE FUNCTIONS
    private fun enablePhone() {
        val active = deviceManger!!.isAdminActive(compName!!)
        if (active) {
            deviceManger!!.removeActiveAdmin(compName!!)
            //btnEnable!!.text = "Enable"
            //btnLock!!.visibility = View.GONE
        } else {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
            startActivityForResult(intent, RESULT_ENABLE)
        }
    }
    private fun lockPhone() {
        deviceManger!!.lockNow()
    }

    private fun lockdevice(){
        deviceManger = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManger!!.isAdminActive(compName!!)
        if (active) {
            lockPhone()
        } else {
            enablePhone()
        }
    }
    //endregion
// region PHONE CALL FUNCTIONS
    private fun makeCalls(){
        if(phonenumber1==""){
            Toast.makeText(this,"Enter a phone number first.", Toast.LENGTH_SHORT).show()
        }
        else {
            val surf = Intent(Intent.ACTION_CALL, Uri.parse("tel:0$phonenumber1"))
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
    //endregion
// region FLASH FUNCTIONS
    fun flashLightOn() {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
        }
    }

    private fun flashLightOff() {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: CameraAccessException) {
        }
    }

    private fun blinkFlash() {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        val myString = "0101010101010101010101010101010101010101"
        val blinkDelay: Long = 50 //Delay in ms
        for (i in 0..100) {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, true)
            Thread.sleep(blinkDelay)
            cameraManager.setTorchMode(cameraId, false)
            Thread.sleep(blinkDelay)

        }
        /* for (i in 0 until myString.length) {
            if (myString[i] == '0') {
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, true)
                } catch (e: CameraAccessException) {
                }
            } else {
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: CameraAccessException) {
                }
            }
            try {
                Thread.sleep(blinkDelay)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } */
    }
    //endregion
// region DEFAULT FUNCTIONS
    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
    override fun onDestroy(){
        super.onDestroy()
    }
//endregion

//endregion

}