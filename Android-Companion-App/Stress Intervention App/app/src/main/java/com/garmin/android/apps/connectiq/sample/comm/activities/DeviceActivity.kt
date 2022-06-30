/**
 * Copyright (C) 2015 Garmin International Ltd.
 * Subject to Garmin SDK License Agreement and Wearables Application Developer Agreement.
 */
package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import com.garmin.android.connectiq.exception.ServiceUnavailableException
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "DeviceActivity"
private const val EXTRA_IQ_DEVICE = "IQDevice"
private const val COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277"

class DeviceActivity : Activity() {

    companion object {
        fun getIntent(context: Context, device: IQDevice?): Intent {
            val intent = Intent(context, DeviceActivity::class.java)
            intent.putExtra(EXTRA_IQ_DEVICE, device)
            return intent
        }
    }

    var DBhelper: AppDatabase? = null

    private var deviceStatusView: TextView? = null
    private var openAppButtonView: TextView? = null

    private val connectIQ: ConnectIQ = ConnectIQ.getInstance()
    private lateinit var device: IQDevice
    private lateinit var myApp: IQApp

    private var appIsOpen = false
    private val openAppListener = ConnectIQ.IQOpenApplicationListener { device, app, status ->
        Toast.makeText(applicationContext, "App Status: " + status.name, Toast.LENGTH_LONG).show()

        if (status == ConnectIQ.IQOpenApplicationStatus.APP_IS_ALREADY_RUNNING) {
            appIsOpen = true
            openAppButtonView?.setText(R.string.open_app_already_open)
        } else {
            appIsOpen = false
            openAppButtonView?.setText(R.string.open_app_open)
        }
    }

    var notificationManager: NotificationManager? = null
    var notificationCompatBuilder: NotificationCompat.Builder? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        DBhelper = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "HRVdatabase").build()

        device = intent.getParcelableExtra<Parcelable>(EXTRA_IQ_DEVICE) as IQDevice
        myApp = IQApp(COMM_WATCH_ID)
        appIsOpen = false

        Log.d(TAG, "연결된 Device: " + device.friendlyName)

        val deviceNameView = findViewById<TextView>(R.id.devicename)
        deviceStatusView = findViewById(R.id.devicestatus)
        openAppButtonView = findViewById(R.id.openapp)
        val openSensorView = findViewById<View>(R.id.taptosee)
        val intentSensor = Intent(this, SensorActivity::class.java)

        deviceNameView?.text = device.friendlyName
        deviceStatusView?.text = device.status?.name
        openAppButtonView?.setOnClickListener { openMyApp() }
        openSensorView?.setOnClickListener { startActivity(intentSensor) }

    }

    public override fun onResume() {
        super.onResume()
        listenByDeviceEvents()
        listenByMyAppEvents()
        getMyAppStatus()
    }

    public override fun onPause() {
        super.onPause()

        // It is a good idea to unregister everything and shut things down to
        // release resources and prevent unwanted callbacks.
        try {
            connectIQ.unregisterForDeviceEvents(device)
            connectIQ.unregisterForApplicationEvents(device, myApp)
        } catch (e: InvalidStateException) {
        }
    }

    private fun openMyApp() {
        Toast.makeText(this, "Opening app...", Toast.LENGTH_SHORT).show()

        // Send a message to open the app
        try {
            connectIQ.openApplication(device, myApp, openAppListener)
        } catch (ex: Exception) {
            Log.e(TAG, ex.toString());
        }
    }

    private fun listenByDeviceEvents() {
        // Get our instance of ConnectIQ. Since we initialized it
        // in our MainActivity, there is no need to do so here, we
        // can just get a reference to the one and only instance.
        try {
            connectIQ.registerForDeviceEvents(device) { device, status ->
                // Since we will only get updates for this device, just display the status
                deviceStatusView?.text = status.name
            }
        } catch (e: InvalidStateException) {
            Log.wtf(TAG, "InvalidStateException:  We should not be here!")
        }
    }


    // Let's register to receive messages from our application on the device.
    private fun listenByMyAppEvents() {
        try {
            connectIQ.registerForAppEvents(device, myApp) { device, app, message, status ->
                // We know from our Comm sample widget that it will only ever send us strings, but in case
                // we get something else, we are simply going to do a toString() on each object in the
                // message list.
                val builder = StringBuilder()
                if (message.size > 0) {
                    for (o in message) {
                        builder.append(o.toString())
                        builder.append("\r\n")
                    }
                } else {
                    builder.append("Received an empty message from the application")
                }

                Log.d(TAG, "받은 데이터 메시지$builder")

                try {
                    giveFeedBack(builder.toString())

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
            }
        } catch (e: InvalidStateException) {
            Toast.makeText(this, "ConnectIQ is not in a valid state", Toast.LENGTH_SHORT).show()
        }
    }

    // Let's check the status of our application on the device.
    private fun getMyAppStatus() {
        try {
            connectIQ.getApplicationInfo(COMM_WATCH_ID, device, object :
                ConnectIQ.IQApplicationInfoListener {
                override fun onApplicationInfoReceived(app: IQApp) {
                    // This is a good thing. Now we can show our list of message options.
                    //buildMessageList()
                }

                override fun onApplicationNotInstalled(applicationId: String) {
                    // The Comm widget is not installed on the device so we have
                    // to let the user know to install it.
                    AlertDialog.Builder(this@DeviceActivity)
                        .setTitle(R.string.missing_widget)
                        .setMessage(R.string.missing_widget_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .create()
                        .show()
                }
            })
        } catch (e1: InvalidStateException) {
        } catch (e1: ServiceUnavailableException) {
        }
    }

    private fun parseSensorData(rawDatas: String): List<Int> {
        try {
            val sensorDataValues = rawDatas.substring(rawDatas.indexOf("[") + 1, rawDatas.indexOf("]")).replace(" ", "").split(",").map{it.toInt()}
            Log.d(TAG, "Parsed Sensor Data Values: $sensorDataValues")

            return sensorDataValues
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, e.toString())
        } catch (e: NumberFormatException){
            Log.e(TAG, e.toString())
        }

        return listOf(0)
    }

    private fun IBItoHRV(IBI_samples: List<Int>): Double{
        var receivedHRVdata = 0.0
        for(i in 0 until (IBI_samples.size-1)){
            receivedHRVdata += (IBI_samples[i + 1] - IBI_samples[i]).toDouble().pow(2.0)
        }

        if(IBI_samples.size > 1) {
            receivedHRVdata /= (IBI_samples.size - 1)
        }
        val realHRVdata = sqrt(receivedHRVdata)

        //TODO: DB 데이터 추가를 따로 다른 메서드로 빼기
        val addRunnable = Runnable {
            DBhelper!!.roomDAO().insert(HRVdata(java.sql.Timestamp(System.currentTimeMillis()).toString(), realHRVdata))
        }

        val thread = Thread(addRunnable)
        thread.start()

        return realHRVdata
    }

    private fun isLowerHRV(userHRV: Double): Boolean {
        val MIN_HRV_1 = 20
        val MIN_HRV_2 = 0

        if(userHRV < MIN_HRV_1 && userHRV > MIN_HRV_2){
            return true
        }

        return false
    }

    private fun giveFeedBack(rawDatas: String){
        if(isLowerHRV(IBItoHRV(parseSensorData(rawDatas)))){
            with(NotificationManagerCompat.from(this)) {
                notificationCompatBuilder?.build()?.let { notify(0, it) }
            }
        } else {
            Log.d(TAG, "No feedback")
        }
    }

}