package com.macapps.go
// region imports

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.io.*
import kotlin.system.exitProcess


//endregion
class PrimaryActivity : AppCompatActivity() {
companion object {
        const val RESULT_ENABLE = 1
    }
// region variables
    private val IGNORE_BATTERY_OPTIMIZATION_REQUEST: Int = 1002
    lateinit var mAdminComponentName: ComponentName
    lateinit var mDevicePolicyManager: DevicePolicyManager
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var deviceManger: DevicePolicyManager? = null
    var compName: ComponentName? = null
    private val PERMISSION_ID = 42
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
//endregion
/*-------------END OF VARIABLE DECLARATION------------------*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary)
// region GET PERMISSIONS
        var permissions: Array<String> = arrayOf(Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
        ActivityCompat.requestPermissions(this,permissions,1)
//endregion
// region PRE-FUNCTIONS-1
        mAdminComponentName = DeviceAdmin.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        mDevicePolicyManager.removeActiveAdmin(mAdminComponentName)
        connectivity= getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val sharedPref = getSharedPreferences("key1", Context.MODE_PRIVATE)
        address=sharedPref.getString("address", "")
        hidefolder=sharedPref.getString("hidefolder", "")
        phonelock=sharedPref.getBoolean("phonelock", true)
        sendlocation=sharedPref.getBoolean("sendlocation", true)
        autodial=sharedPref.getBoolean("autodial", true)
        val gobtn=findViewById<Button>(R.id.primarybutton)
//endregion
//region PRE-FUNCTIONS-2
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
    var bm: BatteryManager=getSystemService(Service.BATTERY_SERVICE) as BatteryManager
    batteryPct = bm.getIntProperty (BatteryManager.BATTERY_PROPERTY_CAPACITY).toDouble()
    capacity=getBatteryCapacity()
    deviceManger = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
    compName = ComponentName(this, DeviceAdmin::class.java)
    val active = deviceManger!!.isAdminActive(compName!!)
    if (!active) {
        enablePhone()
    }
//endregion
//region PRE-FUNCTIONS-3

    //check for BatteryOptimization,

    //check for BatteryOptimization,
    val pm = getSystemService(POWER_SERVICE) as PowerManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            askIgnoreOptimization()
        }
    }

    //start the service

    //start the service
    val sensorService = SensorService()
    val intent = Intent(this, sensorService::class.java)
    if (!isMyServiceRunning(sensorService::class.java)) {
        startService(intent)
    }
//endregion
// region GO BUTTON
    gobtn.setOnClickListener {

            if(clickcount==0){
                gobtn.text = "STOP"
                gobtn.setBackgroundColor(Color.parseColor("#F44336"))
                if(sendlocation==true){
                    sendURL()
                }
                if(autodial==true){
                    makeCalls()
                }
                if(phonelock==true) {
                    lockdevice()
                }
            }
            else{
                gobtn.text="GO"
                if(isDarkTheme(this@PrimaryActivity)) {
                    gobtn.setBackgroundColor(Color.parseColor("#FF03DAC5"))
                }
                else{
                    gobtn.setBackgroundColor(Color.parseColor("#FF018786"))
                }
            }
            clickcount++
            clickcount%=2
        }
//endregion
// region OTHER FUNCTIONS
        val settingsbtn=findViewById<Button>(R.id.settingsbutton)
        settingsbtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val smsbutton=findViewById<Button>(R.id.smsbutton)
        smsbutton.setOnClickListener {
            sendURL()
        }

        val lockbtn=findViewById<Button>(R.id.phonelockbutton)
        lockbtn.setOnClickListener {
            lockdevice()
        }

        val helpbtn=findViewById<Button>(R.id.helpbutton)
        helpbtn.setOnClickListener {
            val intent = Intent(this, HelplineActivity::class.java)
            startActivity(intent)
        }

        val toolsbtn=findViewById<Button>(R.id.toolsbutton)
        toolsbtn.setOnClickListener {
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }

        var backbtn: Button =findViewById(R.id.backbutton9)
        backbtn.setOnClickListener(){
            finish()
            exitProcess(0)
        }
//endregion

}
// region FUNCTIONS
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
                        this@PrimaryActivity,
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
                                    Toast.makeText(this,"Enter atleast one phone number to send a text message.",Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    val list: ArrayList<String?> = arrayListOf(phonenumber1,phonenumber2,phonenumber3,phonenumber4,phonenumber5,phonenumber6,phonenumber7,phonenumber8,phonenumber9,phonenumber10)
                                    //send SMS to each contact
                                    for (c in list) {
                                        if(c!="")
                                        smsManager.sendTextMessage(
                                            c,
                                            null,
                                            message,
                                            null,
                                            null
                                        )
                                    }
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
        Toast.makeText(this,"Enter a phone number first.",Toast.LENGTH_SHORT).show()
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
//region OPTIMIZATION FUNCTIONS
fun askIgnoreOptimization() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        @SuppressLint("BatteryLife") val intent =
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST)
    }
}
    //endregion
//region SERVICE FUNCTIONS
    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }
    //endregion
// region DEFAULT FUNCTIONS
fun isDarkTheme(activity: Activity): Boolean {
    return activity.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}
    override fun onDestroy() {

        super.onDestroy()
    }
//endregion
//endregion

}


