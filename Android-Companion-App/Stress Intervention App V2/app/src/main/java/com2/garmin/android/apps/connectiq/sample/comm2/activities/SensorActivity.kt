package com2.garmin.android.apps.connectiq.sample.comm2.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com2.garmin.android.apps.connectiq.sample.comm2.R
import com2.garmin.android.apps.connectiq.sample.comm2.mobilephonedata.AccService
import com2.garmin.android.apps.connectiq.sample.comm2.mobilephonedata.Constants
import com2.garmin.android.apps.connectiq.sample.comm2.mobilephonedata.LocationService
import com2.garmin.android.apps.connectiq.sample.comm2.mobilephonedata.PhoneUsageService


class SensorActivity : Activity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 1
    private val REQUEST_CODE_PU_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        findViewById<View>(R.id.buttonStartLocationUpdates).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@SensorActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE_LOCATION_PERMISSION
                    )
                } else {
                    startLocationService()
                }
        }

        findViewById<View>(R.id.buttonStopLocationUpdates).setOnClickListener {
            stopLocationService()
        }

        findViewById<View>(R.id.buttonStartAccUpdates).setOnClickListener {
            startAccService()
        }

        findViewById<View>(R.id.buttonStopAccUpdates).setOnClickListener {
            stopAccService()
        }

        findViewById<View>(R.id.buttonStartPUUpdates).setOnClickListener {
            if (!checkPermission()) {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            } else {
                startPUService()
            }
        }

        findViewById<View>(R.id.buttonStopPUUpdates).setOnClickListener {
            stopPUService()
        }



    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (LocationService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startLocationService() {
        if (!isLocationServiceRunning()) {
            val intent = Intent(applicationContext, LocationService::class.java)
            intent.action = Constants.ACTION_START_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopLocationService() {
        if (isLocationServiceRunning()) {
            val intent = Intent(applicationContext, LocationService::class.java)
            intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAccServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (AccService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startAccService() {
        if (!isAccServiceRunning()) {
            val intent = Intent(applicationContext, AccService::class.java)
            intent.action = Constants.ACTION_START_ACC_SERVICE
            startService(intent)
            Toast.makeText(this, "Acc service started", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopAccService() {
        if (isAccServiceRunning()) {
            val intent = Intent(applicationContext, AccService::class.java)
            intent.action = Constants.ACTION_STOP_ACC_SERVICE
            startService(intent)
            Toast.makeText(this, "Acc service stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPUServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
                if (PhoneUsageService::class.java.name == service.service.className) {
                    if (service.foreground) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    }

    private fun startPUService() {
        if (!isPUServiceRunning()) {
            val intent = Intent(applicationContext, PhoneUsageService::class.java)
            intent.action = Constants.ACTION_START_PU_SERVICE
            startService(intent)
            Toast.makeText(this, "Phone Usage service started", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopPUService() {
        if (isPUServiceRunning()) {
            val intent = Intent(applicationContext, PhoneUsageService::class.java)
            intent.action = Constants.ACTION_STOP_PU_SERVICE
            startService(intent)
            Toast.makeText(this, "Phone Usage service stopped", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(): Boolean {
        var granted = false
        val appOps = applicationContext
            .getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), applicationContext.packageName
        )
        granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            applicationContext.checkCallingOrSelfPermission(
                Manifest.permission.PACKAGE_USAGE_STATS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
        return granted
    }

}