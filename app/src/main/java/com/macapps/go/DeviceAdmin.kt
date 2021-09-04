package com.macapps.go

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class DeviceAdmin : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show()
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        super.onDisableRequested(context, intent)
        return "Do you really want to Disable GO ?"

    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show()
    }
    companion object {
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, DeviceAdmin::class.java)
        }

        private val TAG = DeviceAdmin::class.java.simpleName
    }
    override fun onLockTaskModeEntering(context: Context, intent: Intent, pkg: String) {
        super.onLockTaskModeEntering(context, intent, pkg)
        Log.d(TAG, "onLockTaskModeEntering")
    }

    override fun onLockTaskModeExiting(context: Context, intent: Intent) {
        super.onLockTaskModeExiting(context, intent)
        Log.d(TAG, "onLockTaskModeExiting")
    }
}