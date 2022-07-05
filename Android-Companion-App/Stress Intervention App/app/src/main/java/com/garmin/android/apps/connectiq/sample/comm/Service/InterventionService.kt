package com.garmin.android.apps.connectiq.sample.comm.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.activities.InterventionActivity
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import java.lang.Math.sqrt
import java.sql.Timestamp
import kotlin.math.pow


class InterventionService : Service() {

    companion object {
        private const val TAG = "BgService"
        private const val EXTRA_IQ_DEVICE = "IQDevice"
        private const val COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277"

        fun putIntent(context: Context, device: IQDevice?): Intent {
            val intent = Intent(context, InterventionService::class.java)
            intent.putExtra(EXTRA_IQ_DEVICE, device)
            return intent
        }
    }

    private val connectIQ: ConnectIQ = ConnectIQ.getInstance()
    private lateinit var device: IQDevice
    private lateinit var myApp: IQApp
    private lateinit var DBhelper: AppDatabase

    private lateinit var notificationManager: NotificationManager
    private val GROUP_KEY_NOTIFY = "group_key_notify"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //서비스는 한번 실행되면 계속 실행된 상태로 유지되기 때문에 onCreate()에서 intent를 받아 처리하기에는 적절하지 않음
        //따라서 intent에 대한 처리는 onStartCommand()에서 수행

        // Notification 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Stress Intervention"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("channel_1", name, importance)

            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            //TODO: Oreo 이하에서의 처리
        }

        val builder = NotificationCompat.Builder(this, "channel_1")
            .setSmallIcon(R.drawable.ic_wind)
            .setGroup(GROUP_KEY_NOTIFY)
        startForeground(1, builder.build())

        DBhelper = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "HRVdatabase").build()
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
            return START_NOT_STICKY //시작하기에 충분한 정보가 넘어오지 않은 경우 재시작 없이 서비스 종료
        } else {
            // intent가 존재하며, 서비스가 시작되지 않은 경우 때 연결된 기기 이름 정보 파싱
            device = intent.getParcelableExtra<Parcelable>(EXTRA_IQ_DEVICE) as IQDevice
            myApp = IQApp(COMM_WATCH_ID)
            Log.d(TAG, "connected Device: " + device.friendlyName)
            listenByMyAppEvents()
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
            DBhelper.roomDAO().insert(HRVdata(Timestamp(System.currentTimeMillis()).toString(), realHRVdata))
        }

        val thread = Thread(addRunnable)
        thread.start()

        return realHRVdata
    }

    private fun isLowerHRV(userHRV: Double): Boolean {
        val MIN_HRV_1 = 20
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

            notificationManager.notify(2, builder.build())

            //진동 설정(0.5초 진동)
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(500)
            }

        } else {
            Log.d(TAG, "No feedback")
        }
    }

}