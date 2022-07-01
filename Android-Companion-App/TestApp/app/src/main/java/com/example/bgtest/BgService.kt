package com.example.bgtest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BgService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // 오레오 부터는 notification channel을 설정해 주어야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("channel_1", name, importance)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // TODO : 아래 주석 인텐트는 해당 Notification을 눌렀을때 어떤 엑티비티를 띄울 것인지 정의.
        //val notificationIntent = Intent(this, MainActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(this, "channel_1")
            .setSmallIcon(R.drawable.ic_test_notification)
            .setContentText("test")
        //    .setContentIntent(pendingIntent)
        startForeground(1, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO : 서비스 처음 시작시 할 동작 정의.
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        // TODO : 서비스 종료시 할 것들
        super.onDestroy()
    }
}