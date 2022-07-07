package com.garmin.android.apps.connectiq.sample.comm.Service

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.garmin.android.apps.connectiq.sample.comm.activities.SensorActivity
import com.garmin.android.apps.connectiq.sample.comm.roomdb.Accdata
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase
import java.sql.Timestamp

class AccelService : Service(), SensorEventListener {
    companion object{
        private const val TAG = "AccelService"
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var notificationManager: NotificationManager
    private var count: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //notification 설정
        val channelId = "acc_notification_channel"
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(this, SensorActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle("Acc Service")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (this::notificationManager.isInitialized && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Acc Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by acc service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        //foreground 서비스 시작
        startForeground(Constants.ACC_SERVICE_ID, builder.build())

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null){
            Log.d(TAG, "there is no intent")
            return START_NOT_STICKY //시작하기에 충분한 정보가 넘어오지 않은 경우 재시작 없이 서비스 종료
        }

        //sensor manager 설정
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            val mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            //sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, mAccelerometer, 10000000 * 200)

        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            Log.d(
                TAG,
                " x:${event.values[0]}, y:${event.values[1]}, z:${event.values[2]} "
            )
            ++count
            if (count >= 360){
                Log.d(TAG, "count 초기화")
                count = 0

                val addRunnable = Runnable {
                    AppDatabase.getInstance(this).roomDAO().insertAccData(
                            Timestamp(System.currentTimeMillis()).toString(),
                            event.values[0],
                            event.values[1],
                            event.values[2]
                        )
                }
                val thread = Thread(addRunnable)
                thread.start()
            }
            //TODO: DB에 정보 추가



            //Handler(Looper.getMainLooper()).postDelayed(addRunnable, 1000)
            /*
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    // 1초 후 실행할 코드
                    DBhelper!!.roomDAO().insert(
                        Accdata(
                            Timestamp(System.currentTimeMillis()).toString(),
                            event.values[0],
                            event.values[1],
                            event.values[2]
                        )
                    )
                    // Call the Runnable again to execute repeatedly
                    Handler().postDelayed(this, 1000)
                }
            }, 1000)
            */
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //not yet implemented
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

}