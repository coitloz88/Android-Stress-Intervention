package com2.garmin.android.apps.connectiq.sample.comm2.mobilephonedata

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com2.garmin.android.apps.connectiq.sample.comm2.roomdb.Accdata
import com2.garmin.android.apps.connectiq.sample.comm2.roomdb.AppDatabase3
import com2.garmin.android.apps.connectiq.sample.comm2.roomdb.AppDatabase4
import com2.garmin.android.apps.connectiq.sample.comm2.roomdb.Locationdata
import java.sql.Timestamp


class AccService : Service(), SensorEventListener {
    var DBhelper: AppDatabase4? = null

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun startAccService() {
        val channelId = "acc_notification_channel"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.mipmap.sym_def_app_icon)
        builder.setContentTitle("Acc Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Acc Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by acc service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            30*1000000)
        startForeground(Constants.ACC_SERVICE_ID, builder.build())
    }

    private fun stopAccService() {
        sensorManager.unregisterListener(this)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == Constants.ACTION_START_ACC_SERVICE) {
                    startAccService()
                } else if (action == Constants.ACTION_STOP_ACC_SERVICE) {
                    stopAccService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        DBhelper = Room.databaseBuilder(applicationContext, AppDatabase4::class.java, "Accdatabase").build()
        event?.let {
            Log.d("SensorActivity", " x:${event.values[0]}, y:${event.values[1]}, z:${event.values[2]} ")
            val addRunnable = Runnable {
                DBhelper!!.roomDAO().insert(Accdata(Timestamp(System.currentTimeMillis()).toString(), event.values[0], event.values[1], event.values[2]))
            }
            val thread = Thread(addRunnable)
            thread.start()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}