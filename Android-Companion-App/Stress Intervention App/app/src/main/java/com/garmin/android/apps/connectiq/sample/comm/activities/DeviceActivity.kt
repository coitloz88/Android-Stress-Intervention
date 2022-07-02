/**
 * Copyright (C) 2015 Garmin International Ltd.
 * Subject to Garmin SDK License Agreement and Wearables Application Developer Agreement.
 */
package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.*
import android.content.Context
import android.content.DialogInterface
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
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase2
import com.garmin.android.apps.connectiq.sample.comm.roomdb.ESMdata
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import com.garmin.android.connectiq.exception.ServiceUnavailableException
import java.lang.IllegalArgumentException
import java.sql.Timestamp
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "DeviceActivity"
private const val EXTRA_IQ_DEVICE = "IQDevice"
private const val COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277"

// TODO Add a valid store app id.
private const val STORE_APP_ID = ""

class DeviceActivity : Activity() {

    companion object {
        fun getIntent(context: Context, device: IQDevice?): Intent {
            val intent = Intent(context, DeviceActivity::class.java)
            intent.putExtra(EXTRA_IQ_DEVICE, device)
            return intent
        }
    }

    var DBhelper1: AppDatabase1? = null
    var DBhelper2: AppDatabase2? = null

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

        DBhelper1 = Room.databaseBuilder(applicationContext, AppDatabase1::class.java, "HRVdatabase").build()
        DBhelper2 = Room.databaseBuilder(applicationContext, AppDatabase2::class.java, "ESMdatabase").build()

        device = intent.getParcelableExtra<Parcelable>(EXTRA_IQ_DEVICE) as IQDevice
        myApp = IQApp(COMM_WATCH_ID)
        appIsOpen = false

        Log.d(TAG, "연결된 Device: " + device.friendlyName)

        val deviceNameView = findViewById<TextView>(R.id.devicename)
        deviceStatusView = findViewById(R.id.devicestatus)
        openAppButtonView = findViewById(R.id.openapp)
        val openAppStoreView = findViewById<View>(R.id.openstore)
        val openSensorView = findViewById<View>(R.id.taptosee)
        val intentSensor = Intent(this, SensorActivity::class.java)

        deviceNameView?.text = device.friendlyName
        deviceStatusView?.text = device.status?.name
        openAppButtonView?.setOnClickListener { openMyApp() }
        openAppStoreView?.setOnClickListener { openStore() }
        openSensorView?.setOnClickListener { startActivity(intentSensor) }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Notification Channel 아이디, 이름, 설명, 중요도 설정
            val channelId = "channel_one"
            val channelName = "Channel One"
            val channelDescription = "Channel One Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            // NotificationChannel 객체 생성
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            // 설명 설정
            notificationChannel.description = channelDescription
            // 시스템에 notificationChannel 등록
            notificationManager.createNotificationChannel(notificationChannel)
            notificationCompatBuilder = NotificationCompat.Builder(this, channelId)
        }
        else {
            // 26버전 미만은 생성자에 context만 설정
            notificationCompatBuilder = NotificationCompat.Builder(this)
        }

        notificationCompatBuilder?.let {
            it.setSmallIcon(android.R.drawable.ic_notification_overlay)
            it.setWhen(System.currentTimeMillis())
            it.setContentTitle("Hey!")
            it.setContentText("Take a breath :)")
            it.setDefaults(Notification.DEFAULT_VIBRATE)
            it.setAutoCancel(true)
            it.setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.drawable.breath)))
        }

        // Create an explicit intent for an Activity in your app
        val intentIntervention = Intent(this, InterventionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentIntervention, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationCompatBuilder?.setContentIntent(pendingIntent)
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

    private fun openStore() {
        /*
        Toast.makeText(this, "Opening ConnectIQ Store...", Toast.LENGTH_SHORT).show()
        // Send a message to open the store
        try {
            if (STORE_APP_ID.isBlank()) {
                AlertDialog.Builder(this@DeviceActivity)
                    .setTitle(R.string.missing_store_id)
                    .setMessage(R.string.missing_store_id_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()
            } else {
                connectIQ.openStore(STORE_APP_ID)
            }
        } catch (ex: Exception) {
        }
        */
        val alertBuilder = AlertDialog.Builder(this@DeviceActivity)
        val arrayEMA = arrayOf("Yes", "No")
        alertBuilder.setTitle("Are you stressed now?")
        alertBuilder.setSingleChoiceItems(arrayEMA, -1,
        ) { dialog, which ->
            val choice = arrayEMA[which]
            try {
                val addRunnable2 = Runnable {
                    DBhelper2!!.roomDAO().insert(ESMdata(System.currentTimeMillis().toString(), choice))
                    Log.d(TAG, "Choice is "+choice)
                }
                val thread2 = Thread(addRunnable2)
                thread2.start()
                dialog.dismiss()
            } catch (e:IllegalArgumentException) {
                Toast.makeText(this, "Please select your answer", Toast.LENGTH_SHORT).show()
            }
        }
        alertBuilder.create()
        alertBuilder.show()
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
                var idCount = 0
                if (message.size > 0) {
                    for (o in message) {
                        builder.append(o.toString())
                        builder.append("\r\n")
                    }
                } else {
                    builder.append("Received an empty message from the application")
                }

                Log.d(TAG, "받은 데이터 메시지" + builder.toString())
                /*
                AlertDialog.Builder(this@DeviceActivity)
                    .setTitle(R.string.received_message)
                    .setMessage(builder.toString())
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show()
                */

                try {
                    giveFeedBack(builder.toString())
                    //idCount += 1

                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
                /*
                val alertBuilder = AlertDialog.Builder(this@DeviceActivity)
                val arrayEMA = arrayOf("Yes", "No")
                alertBuilder.setTitle("Are you stressed now?")
                alertBuilder.setSingleChoiceItems(arrayEMA, -1,
                ) { dialog, which ->
                    val choice = arrayEMA[which]
                    try {
                        val addRunnable = Runnable {
                            DBhelper!!.roomDAO().insertEMAdata(idCount, choice)
                        }
                        val thread = Thread(addRunnable)
                        thread.start()
                        dialog.dismiss()
                    } catch (e:IllegalArgumentException) {
                        Toast.makeText(this, "Please select your answer", Toast.LENGTH_SHORT).show()
                    }
                }
                alertBuilder.create()
                alertBuilder.show()
                */
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

    /*
    private fun buildMessageList() {
        val adapter = MessagesAdapter { onItemClick(it) }
        adapter.submitList(MessageFactory.getMessages(this@DeviceActivity))
        findViewById<RecyclerView>(android.R.id.list).apply {
            layoutManager = LinearLayoutManager(this@DeviceActivity)
            this.adapter = adapter
        }
    }

    private fun onItemClick(message: Any) {
        try {
            connectIQ.sendMessage(device, myApp, message) { device, app, status ->
                Toast.makeText(this@DeviceActivity, status.name, Toast.LENGTH_SHORT).show()
            }
        } catch (e: InvalidStateException) {
            Toast.makeText(this, "ConnectIQ is not in a valid state", Toast.LENGTH_SHORT).show()
        } catch (e: ServiceUnavailableException) {
            Toast.makeText(
                this,
                "ConnectIQ service is unavailable.   Is Garmin Connect Mobile installed and running?",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    */

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

        val addRunnable = Runnable {
            DBhelper1!!.roomDAO().insert(HRVdata(Timestamp(System.currentTimeMillis()).toString(), realHRVdata))
        }

        val thread = Thread(addRunnable)
        thread.start()

        return realHRVdata
    }

    private fun isLowerHRV(userHRV: Double): Boolean {
        val MIN_HRV_1 = 20 // TODO: 설정해주기
        val MIN_HRV_2 = 0 // TODO: 설정해주기

        if(userHRV < MIN_HRV_1 && userHRV > MIN_HRV_2){
            return true
        }

        return false
    }

    private fun giveFeedBack(rawDatas: String){
        if(isLowerHRV(IBItoHRV(parseSensorData(rawDatas)))){
            /*
            Log.d(TAG, "return feedback to Garmin Watch app")
            openMyApp() // 앱을 foreground로 가져옴

            val feedbackMessage = "Take a Breath."

            try {
                connectIQ.sendMessage(device, myApp, feedbackMessage) { device, app, status ->
                    Toast.makeText(this@DeviceActivity, status.name, Toast.LENGTH_SHORT).show()
                }
            } catch (e: InvalidStateException) {
                Toast.makeText(this, "ConnectIQ is not in a valid state", Toast.LENGTH_SHORT).show()
            } catch (e: ServiceUnavailableException) {
                Toast.makeText(
                    this,
                    "ConnectIQ service is unavailable.   Is Garmin Connect Mobile installed and running?",
                    Toast.LENGTH_LONG
                ).show()
            }
            */

            with(NotificationManagerCompat.from(this)) {
                notificationCompatBuilder?.build()?.let { notify(0, it) }
            }

        } else {
            Log.d(TAG, "No feedback")
        }
    }

}