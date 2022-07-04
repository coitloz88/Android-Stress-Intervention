package com2.garmin.android.apps.connectiq.sample.comm2.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
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


class SensorActivity : Activity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 1

    /*private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }*/

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

    }

    override fun onResume() {
        super.onResume()
        /*sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)*/
    }

    /*
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // [0] x축값, [1] y축값, [2] z축값
            Log.d("SensorActivity", " x:${event.values[0]}, y:${event.values[1]}, z:${event.values[2]} ")
            data1?.text = "X =" + event.values[0]
            data2?.text = "Y =" + event.values[1]
            data3?.text = "Z =" + event.values[2]
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }*/

    override fun onPause() {
        super.onPause()
        //sensorManager.unregisterListener(this)
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
}