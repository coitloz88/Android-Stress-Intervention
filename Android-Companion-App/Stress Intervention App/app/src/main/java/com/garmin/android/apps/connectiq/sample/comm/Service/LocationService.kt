package com.garmin.android.apps.connectiq.sample.comm.Service

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.garmin.android.apps.connectiq.sample.comm.activities.SensorActivity
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase3
import com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.sql.Timestamp


class LocationService : Service() {
    companion object{
        private const val TAG = "LocationService"
    }

    private lateinit var DBhelper: AppDatabase3
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        val channelId = "location_notification_channel"
        notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(this, SensorActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle("Location Service")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentText("Running")
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Location Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 30*1000
        locationRequest.fastestInterval = 30*1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        DBhelper = AppDatabase3.getInstance(this)
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper())
        startForeground(Constants.LOCATION_SERVICE_ID, builder.build())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult != null && locationResult.lastLocation != null) {
                val latitude = locationResult.lastLocation.latitude
                val longitude = locationResult.lastLocation.longitude
                Log.v("LOCATION_UPDATE", "$latitude, $longitude")
                val addRunnable = Runnable {
                    DBhelper.roomDAO().insert(Locationdata(Timestamp(System.currentTimeMillis()).toString(), latitude, longitude))
                }
                val thread = Thread(addRunnable)
                thread.start()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(intent == null){
            Log.d(TAG, "there is no intent")
            return START_NOT_STICKY //시작하기에 충분한 정보가 넘어오지 않은 경우 재시작 없이 서비스 종료
        }
        Log.d(TAG, "starting Location Service...")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(mLocationCallback)
        super.onDestroy()
        Log.d(TAG, "quit location service")
    }
}