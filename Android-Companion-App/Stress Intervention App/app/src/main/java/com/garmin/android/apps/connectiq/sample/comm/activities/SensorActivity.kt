package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.garmin.android.apps.connectiq.sample.comm.R
import java.text.SimpleDateFormat
import java.util.*

class SensorActivity : Activity(), SensorEventListener {
    private var data1: TextView? = null
    private var data2: TextView? = null
    private var data3: TextView? = null
    //private var data4: TextView? = null

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    /*
    var time = "yyyy/MM/dd_HH:mm:ss"
    var currentTime: Date = Calendar.getInstance().getTime()
    var format: SimpleDateFormat = SimpleDateFormat(time, Locale.getDefault())
    var current: String = format.format(currentTime)
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        data1 = findViewById(R.id.data1)
        data2 = findViewById(R.id.data2)
        data3 = findViewById(R.id.data3)
        //data4 = findViewById(R.id.data4)

        //data4?.text = current
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

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
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}