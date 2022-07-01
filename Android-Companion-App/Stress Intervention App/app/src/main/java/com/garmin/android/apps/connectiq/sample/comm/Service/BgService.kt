package com.garmin.android.apps.connectiq.sample.comm.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice

class BgService : Service() {

    companion object {
        private const val TAG = "BgService"
        private const val EXTRA_IQ_DEVICE = "IQDevice"
        private const val COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277"

        fun putIntent(context: Context, device: IQDevice?): Intent {
            val intent = Intent(context, BgService::class.java)
            intent.putExtra(EXTRA_IQ_DEVICE, device)
            return intent
        }
    }

    private val connectIQ: ConnectIQ = ConnectIQ.getInstance()
    private lateinit var device: IQDevice
    private lateinit var myApp: IQApp

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //서비스는 한번 실행되면 계속 실행된 상태로 유지외기 때문에 intent를 받아 처리하기에는 적절하지 않음
        //따라서 intent에 대한 처리는 onStartCommand()에서 수행

        /*        // Notification 설정
        var notificationCompatBuilder: NotificationCompat.Builder? = null
        var notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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

        notificationCompatBuilder.let {
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
        notificationCompatBuilder.setContentIntent(pendingIntent)*/
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
            .setSmallIcon(R.drawable.ic_wind)
            .setContentText("test")
        //    .setContentIntent(pendingIntent)
        startForeground(1, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null){
            Log.d(TAG, "there is no data of intent")
            return START_STICKY //서비스가 종료되어도 자동으로 다시 실행
        } else {
            // intent가 존재할 때 저장된 정보 파싱
            device = intent.getParcelableExtra<Parcelable>(EXTRA_IQ_DEVICE) as IQDevice
            myApp = IQApp(COMM_WATCH_ID)
            Log.d(TAG, "connected Device: " + device.friendlyName)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        // TODO : 서비스 종료시 할 것들
        super.onDestroy()
        Log.d(TAG, "Service execution finished")
    }
}