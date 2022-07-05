package com.garmin.android.apps.connectiq.sample.comm.Service

import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.Utils.mPreferences
import com.garmin.android.apps.connectiq.sample.comm.activities.InterventionActivity
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import java.lang.Math.sqrt
import java.sql.Timestamp
import kotlin.math.pow


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
    private lateinit var DBhelper: AppDatabase1

    private lateinit var notificationManager: NotificationManager
    private val GROUP_KEY_NOTIFY = "group_key_notify"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //서비스는 한번 실행되면 계속 실행된 상태로 유지외기 때문에 intent를 받아 처리하기에는 적절하지 않음
        //따라서 intent에 대한 처리는 onStartCommand()에서 수행

        // Notification 설정
/*        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            if(it != null){
                it.setSmallIcon(R.drawable.ic_wind)
                it.setWhen(System.currentTimeMillis())
                it.setContentTitle("Hey!")
                it.setContentText("Take a breath :)")
                it.setDefaults(Notification.DEFAULT_VIBRATE)
                it.setAutoCancel(true)
                it.setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.drawable.breath)))
            }
        }

        // Create an explicit intent for an Activity in your app
        val intentIntervention = Intent(this, InterventionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intentIntervention, PendingIntent.FLAG_UPDATE_CURRENT)

        if(notificationCompatBuilder == null){
            Log.e(TAG, "Null Pointer Exception: notificationCompatBuilder is null")
            stopSelf()
        } else {
            notificationCompatBuilder!!.setContentIntent(pendingIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("channel_1", name, importance)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 아래 주석 인텐트는 해당 Notification을 눌렀을때 어떤 엑티비티를 띄울 것인지 정의.
        //val notificationIntent = Intent(this, InterventionActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(this, "channel_1")
            .setSmallIcon(R.drawable.ic_wind)
            .setContentText("intervention message test")
           // .setContentIntent(pendingIntent)
        startForeground(1, builder.build())*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Stress Intervention"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("channel_1", name, importance)

            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 아래 인텐트는 해당 Notification을 눌렀을때 어떤 엑티비티를 띄울 것인지 정의.
        // val notificationIntent = Intent(this, InterventionActivity::class.java)
        // val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "channel_1")
            .setSmallIcon(R.drawable.ic_wind)
            .setGroup(GROUP_KEY_NOTIFY)
        //    .setContentText("test message")
        //    .setContentIntent(pendingIntent)
        startForeground(1, builder.build())

        DBhelper = Room.databaseBuilder(applicationContext, AppDatabase1::class.java, "HRVdatabase").build()
    }

    private fun listenByMyAppEvents() {
        //이벤트 리스너 설정
        try {
            Log.d(TAG, "registering garmin app events...")
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
                    Log.d(TAG, "Received data from Garmin Watch: $builder")
                    giveFeedback(builder.toString())
                } else {
                    builder.append("Received an empty message from the application")
                }


            }
        } catch (e: InvalidStateException) {
            Log.e(TAG, "ConnectIQ is not in a valid state")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null || !intent.hasExtra(EXTRA_IQ_DEVICE)){
            Log.d(TAG, "there is no intent")
            //return START_REDELIVER_INTENT //서비스가 종료되어도 자동으로 이전 intent 정보를 가지고 다시 실행
            return START_NOT_STICKY
        } else {
            if(mPreferences.prefs.getBoolean("isIntervention", true)){
                Log.d(TAG, "intervention process is already running")
            } else {
                // intent가 존재하며, 서비스가 시작되지 않은 경우 때 연결된 기기 이름 정보 파싱
                device = intent.getParcelableExtra<Parcelable>(EXTRA_IQ_DEVICE) as IQDevice
                myApp = IQApp(COMM_WATCH_ID)
                Log.d(TAG, "connected Device: " + device.friendlyName)
                mPreferences.prefs.setBoolean("isIntervention", true)
                listenByMyAppEvents()         // TODO: 이벤트 리스너 등록을 언제 해야하지?
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        // TODO : 서비스 종료시 할 것들(현재 shutdown..을 하는 경우 익셉션 발생
        try{
            connectIQ.unregisterForDeviceEvents(device)
            connectIQ.unregisterForApplicationEvents(device, myApp)
            connectIQ.unregisterAllForEvents()
            //connectIQ.shutdown(applicationContext)
        } catch (e: InvalidStateException) {
            Log.e(TAG, e.toString())
        }

        super.onDestroy()
        Log.d(TAG, "Service execution finished")
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

        val addRunnable = Runnable {
            DBhelper!!.roomDAO().insert(HRVdata(Timestamp(System.currentTimeMillis()).toString(), realHRVdata))
        }

        val thread = Thread(addRunnable)
        thread.start()

        return realHRVdata
    }

    private fun isLowerHRV(userHRV: Double): Boolean {
        val MIN_HRV_1 = 40
        val MIN_HRV_2 = 0

        if(userHRV < MIN_HRV_1 && userHRV > MIN_HRV_2){
            Log.d(TAG, "HRV value is not in normal range")
            return true
        }
        Log.d(TAG, "HRV value is in normal range")
        return false
    }

    private fun giveFeedback(rawDatas: String){
        if(isLowerHRV(IBItoHRV(parseSensorData(rawDatas)))){
            //notification 설정
            val notificationIntent = Intent(this, InterventionActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            val builder = NotificationCompat.Builder(this, "channel_1")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_wind)
                .setContentText("Hey! Take a Breath:)")
                .setContentIntent(pendingIntent)
                .setGroup(GROUP_KEY_NOTIFY)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            notificationManager.notify(101, builder.build())
        } else {
            Log.d(TAG, "No feedback")
        }
    }

}