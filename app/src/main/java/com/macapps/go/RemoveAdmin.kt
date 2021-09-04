package com.macapps.go

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RemoveAdmin : AppCompatActivity() {
    lateinit var mAdminComponentName: ComponentName
    lateinit var mDevicePolicyManager: DevicePolicyManager
    var deviceManger: DevicePolicyManager? = null
    var compName: ComponentName? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdminComponentName = DeviceAdmin.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceManger = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManger!!.isAdminActive(compName!!)
        if(active) {
            mDevicePolicyManager.removeActiveAdmin(mAdminComponentName)
        }
        else{
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            startActivityForResult(intent, PrimaryActivity.RESULT_ENABLE)
        }
        finish()
    }
}