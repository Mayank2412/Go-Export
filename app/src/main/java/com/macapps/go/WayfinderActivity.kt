package com.macapps.go

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
import android.media.SoundPool
import android.os.*
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.text.DecimalFormat
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan


var str: String=""
var dlat: Float=0.0F
var dlong: Float=0.0F
@Suppress("DEPRECATION")
class WayfinderActivity : AppCompatActivity(), SensorEventListener{
    // region variables
    var count1=0
    private lateinit var sensorManager: SensorManager
    private var mRotation: Sensor? = null
    val soundPool= SoundPool(1, android.media.AudioManager.STREAM_ALARM, 0)
    var soundId=1
    var check=0
    val PERMISSION_ID = 42
    var slat=0.0000
    var slong=0.0000
    var angle: Float=0.0F
    lateinit var arrow: ImageView
    var address: String=""
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val locationAddress = GeoCodingLocation()
    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
// region ONCREATE BODY
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wayfinder)
        var sirenbtn: Button=findViewById(R.id.sirenbutton)

        var sharedPref = getSharedPreferences("key1", Context.MODE_PRIVATE)
        address= sharedPref.getString("address","").toString()
        locationAddress.getAddressFromLocation(address, applicationContext,
            GeoCoderHandler()
        )
        var audioManager= getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE,0)
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),0)


//endregion

//region back
        var backbtn: Button=findViewById(R.id.backbutton)
        backbtn.setOnClickListener(){
            finish()
        }

// endregion

// region siren
        var count=0
        soundPool.load(baseContext, R.raw.siren, 1)

        sirenbtn.setOnClickListener {
            if(count==0){
                sirenbtn.text="STOP"
                sirenbtn.setBackgroundColor(Color.parseColor("#F44336"))
                playSound()
                count++
                check=1
            }
            else{
                sirenbtn.text="ALERT"
                sirenbtn.setBackgroundColor(Color.parseColor("#018786"))
                stopSound()
                count++
            }
            count%=2
        }
//endregion

// region sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mRotation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
//endregion

// region location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                    var permissions: Array<String> = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                    ActivityCompat.requestPermissions(this,permissions,1)
                }

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        requestNewLocationData()
                        if(dlat.toString()==""||dlong.toString()==""){
                            Toast.makeText(this, "Enter the co-ordinates properly", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            val df=DecimalFormat("##.######")
                            slat = location.latitude
                            slong = location.longitude
                            val dlatdf=df.format(dlat!!.toFloat()).toFloat()
                            val dlongdf=df.format(dlong!!.toFloat()).toFloat()
                            val slatdf=df.format(slat.toFloat()).toFloat()
                            val slongdf=df.format(slong.toFloat()).toFloat()
                            val latvec= (dlatdf-slatdf)*110570
                            val longvec= (dlongdf-slongdf)*110570
                            angle=(atan(longvec / latvec)*180/ PI).toFloat()
                            arrow=findViewById(R.id.arrow)
                            val Northdist=findViewById<TextView>(R.id.NorthDist)
                            val Southdist=findViewById<TextView>(R.id.SouthDist)
                            val Eastdist=findViewById<TextView>(R.id.EastDist)
                            val Westdist=findViewById<TextView>(R.id.WestDist)
                            lateinit var df1: DecimalFormat
                            if(abs(latvec/1000)>99||abs(longvec/1000)>99) {
                                df1 = DecimalFormat("##.##")
                            }
                            else if(abs(latvec/1000)>999||abs(longvec/1000)>999) {
                                df1 = DecimalFormat("####")
                            }
                            else{
                                df1 = DecimalFormat("##.###")
                            }
                            if(address==""){
                                Northdist.text="0.00 km"
                                Southdist.text="0.00 km"
                                Eastdist.text="0.00 km"
                                Westdist.text="0.00 km"
                                Toast.makeText(this,"Enter an Address.",Toast.LENGTH_SHORT).show()
                            }
                            else {
                                if (latvec < 0) {
                                    Northdist.text = "0.00 km"
                                    Southdist.text = df1.format((abs(latvec) / 1000)) + " km"
                                } else {
                                    Southdist.text = "0.00 km"
                                    Northdist.text = df1.format((abs(latvec) / 1000)) + " km"
                                }
                                if (longvec < 0) {
                                    Eastdist.text = "0.00 km"
                                    Westdist.text = df1.format((abs(longvec) / 1000)) + " km"
                                } else {
                                    Westdist.text = "0.00 km"
                                    Eastdist.text = df1.format((abs(longvec) / 1000)) + " km"
                                }
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
//endregion
    }
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
    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
//endregion

    // region LOCATION FUNCTIONS
    private fun getLastLocation() {
    }
    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0.5.toLong()
        mLocationRequest.fastestInterval = 0
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val df=DecimalFormat("##.######")
            slat = mLastLocation.latitude
            slong = mLastLocation.longitude
            val dlatdf=df.format(dlat.toFloat()).toFloat()
            val dlongdf=df.format(dlong.toFloat()).toFloat()
            val slatdf=df.format(slat.toFloat()).toFloat()
            val slongdf=df.format(slong.toFloat()).toFloat()
            val latvec= (dlatdf-slatdf)*110570
            val longvec= (dlongdf-slongdf)*110570
            angle=(atan(longvec / latvec)*180/ PI).toFloat()
            arrow=findViewById(R.id.arrow)
            val Northdist=findViewById<TextView>(R.id.NorthDist)
            val Southdist=findViewById<TextView>(R.id.SouthDist)
            val Eastdist=findViewById<TextView>(R.id.EastDist)
            val Westdist=findViewById<TextView>(R.id.WestDist)
            var df1: DecimalFormat
            if(abs(latvec/1000)>1000||abs(longvec/1000)>1000) {
                df1 = DecimalFormat("####")
            }
            else{
                df1 = DecimalFormat("##.###")
            }
            if(latvec<0){
                Northdist.text="0.00 km"
                Southdist.text=df1.format((abs(latvec) / 1000))+" km"
            }
            else{
                Southdist.text="0.00 km"
                Northdist.text=df1.format((abs(latvec) / 1000))+" km"
            }
            if(longvec<0){
                Eastdist.text="0.00 km"
                Westdist.text=df1.format((abs(longvec) / 1000))+" km"
            }
            else{
                Westdist.text="0.00 km"
                Eastdist.text=df1.format((abs(longvec) / 1000))+" km"
            }

        }
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this@WayfinderActivity,
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

    // region SENSOR FUNCTIONS
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        var text: TextView=findViewById(R.id.textView4)
        // The light sensor returns a single value.
        var df=DecimalFormat("###.##")
        arrow=findViewById(R.id.arrow)
        // Many sensors return 3 values, one for each axis.
        arrow.rotation=-event.values[0]
        text.text = (df.format(event.values[0])).toString()+" N"
        // Do something with this sensor value.
    }

    override fun onResume() {
        super.onResume()
        mRotation?.also { rotation ->
            sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onStart(){
        super.onStart()
        requestNewLocationData()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
//endregion


    companion object {
        private class GeoCoderHandler : Handler() {
            override fun handleMessage(message: Message) {
                val locationAddress: String? = when (message.what) {
                    1 -> {
                        val bundle = message.data
                        bundle.getString("address")
                    }
                    else -> null
                }
                str = locationAddress.toString()
                val lat: Float = when (message.what) {
                    1 -> {
                        val bundle = message.data
                        bundle.getFloat("lat")
                    }
                    else -> 0.0F
                }
                dlat=lat
                val lng: Float = when (message.what) {
                    1 -> {
                        val bundle = message.data
                        bundle.getFloat("lng")
                    }
                    else -> 0.0F
                }
                dlong=lng

            }
        }
    }


}