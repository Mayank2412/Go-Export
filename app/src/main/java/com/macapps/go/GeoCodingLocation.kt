package com.macapps.go

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.util.*

var lat: Float=0.0F
var lng: Float=0.0F
class GeoCodingLocation {
    private val TAG = "GeoCodeLocation"
    fun getAddressFromLocation(
        locationAddress: String,
        context: Context, handler: Handler
    ) {
        val thread = object : Thread() {
            override fun run() {
                val geoCoder = Geocoder(
                    context,
                    Locale.getDefault()
                )
                var result: String? = null
                try {
                    val addressList = geoCoder.getFromLocationName(locationAddress, 1)
                    if (addressList != null && addressList.size > 0) {
                        val address = addressList.get(0) as Address
                        lat=address.latitude.toFloat()
                        lng=address.longitude.toFloat()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Unable to connect to GeoCoder", e)
                } finally {
                    val message = Message.obtain()
                    message.target = handler
                    message.what = 1
                    val bundle = Bundle()
                    result = ("Address: $locationAddress" +
                            "\n\nLatitude:$lat and Longitude:$lng \n")
                    bundle.putString("address", result)
                    bundle.putFloat("lat",lat)
                    bundle.putFloat("lng",lng)
                    message.data = bundle
                    message.sendToTarget()
                }
            }
        }
        thread.start()
    }
}