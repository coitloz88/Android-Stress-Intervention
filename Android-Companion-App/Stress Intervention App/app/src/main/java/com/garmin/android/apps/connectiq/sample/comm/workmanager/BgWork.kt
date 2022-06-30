package com.garmin.android.apps.connectiq.sample.comm.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.activities.InterventionActivity
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import java.sql.Timestamp
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "BackgroundWork"
private const val EXTRA_IQ_DEVICE = "IQDevice"
private const val COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277"

class BgWork(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    private val connectIQ: ConnectIQ = ConnectIQ.getInstance()
    private lateinit var device: IQDevice
    private lateinit var myApp: IQApp

    private var appIsOpen = false
    private val openAppListener = ConnectIQ.IQOpenApplicationListener { device, app, status ->
        Toast.makeText(applicationContext, "App Status: " + status.name, Toast.LENGTH_LONG).show()

        if (status == ConnectIQ.IQOpenApplicationStatus.APP_IS_ALREADY_RUNNING) {
            appIsOpen = true
            Toast.makeText(applicationContext, "already running", Toast.LENGTH_SHORT)
        } else {
            appIsOpen = false
            Toast.makeText(applicationContext, "opening app...", Toast.LENGTH_SHORT)
        }
    }

    var notificationManager: NotificationManager? = null
    var notificationCompatBuilder: NotificationCompat.Builder? = null

    override fun doWork(): Result {
        //TODO
        /**
         * 1. Room DB 연결
         * 1-1. 알림 설정
         * 2. 가민 워치로부터 데이터 가져오기
         * 3. 가져온 데이터 파싱
         * 4. 정상적인 정보인 경우 DB에 저장
         * 5. 파싱된 값이 적정치가 아니라면 notification
         */
        connectDB()
        listenByMyAppEvents()
        setNotificationSettings()

        return Result.success()
    }

    private fun setNotificationSettings() {
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
        notificationCompatBuilder?.setContentIntent(pendingIntent)    }

    private fun connectDB() {
        //TODO
    }

    private fun listenByMyAppEvents() {
        //TODO
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
                giveFeedBack(builder.toString())
                Log.d(TAG, "Received message from Garmin watch: $builder")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun parseDatasToInt(rawDatas: String): List<Int>{
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

    private fun IBItoHRV(IBI_samples: List<Int>): Double {
        var receivedHRVdata = 0.0
        for (i in 0 until (IBI_samples.size - 1)) {
            receivedHRVdata += (IBI_samples[i + 1] - IBI_samples[i]).toDouble().pow(2.0)
        }

        if (IBI_samples.size > 1) {
            receivedHRVdata /= (IBI_samples.size - 1)
        }

        return sqrt(receivedHRVdata)
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
        if(isLowerHRV(IBItoHRV(parseDatasToInt(rawDatas)))){
            with(NotificationManagerCompat.from(this)) {
                notificationCompatBuilder?.build()?.let { notify(0, it) }
            }
        } else {
            Log.d(TAG, "No feedback")
        }
    }

}