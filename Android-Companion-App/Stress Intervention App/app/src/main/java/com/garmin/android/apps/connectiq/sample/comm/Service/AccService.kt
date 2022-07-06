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
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase4
import java.sql.Timestamp

class AccService : Service(), SensorEventListener {
    companion object{
        private const val TAG = "AccService"
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var DBhelper: AppDatabase4
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //notification 설정
        val channelId = "acc_notification_channel"
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
        builder.setSmallIcon(R.mipmap.sym_def_app_icon)
        builder.setContentTitle("Acc Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
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

        //DB연결
        DBhelper = AppDatabase4.getInstance(this)

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
            sensorManager.registerListener(this, mAccelerometer, 1000000 * 5)

        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            Log.d(
                TAG,
                " x:${event.values[0]}, y:${event.values[1]}, z:${event.values[2]} "
            )
            
            //TODO: DB에 정보 추가
//            val addRunnable = Runnable {
//                DBhelper!!.roomDAO().insert(
//                    Accdata(
//                        Timestamp(System.currentTimeMillis()).toString(),
//                        event.values[0],
//                        event.values[1],
//                        event.values[2]
//                    )
//                )
//            }
//            val thread = Thread(addRunnable)
//            thread.start()


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