package com.garmin.android.apps.connectiq.sample.comm.Service

import android.Manifest
import android.R
import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import android.os.Build
import android.os.Process
import android.util.LongSparseArray
import com.garmin.android.apps.connectiq.sample.comm.BuildConfig
import com.garmin.android.apps.connectiq.sample.comm.activities.SensorActivity

class PhoneUsageService : Service() {
    companion object{
        private const val TAG = "PhoneUsageService"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        Log.d("checkpoint","start")

        //notification 설정
        val channelId = "phone_usage_notification_channel"
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
            .setContentTitle("Phone Usage Service")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentText("Running")
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "Phone Usage Service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by phone usage service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        
        val lastPackageName = getPackageName(applicationContext)
        if (lastPackageName != null) {
            Log.d("PhoneUsageData", lastPackageName)
        }
        startForeground(Constants.PU_SERVICE_ID, builder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            Log.d(TAG, "there is no intent")
            return START_NOT_STICKY //시작하기에 충분한 정보가 넘어오지 않은 경우 재시작 없이 서비스 종료
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "quit phone usage service")
    }

    fun getPackageName(context: Context): String? {
        val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        var lastRunAppTimeStamp = 0L

        // 얼마만큼의 시간동안 수집한 앱의 이름을 가져오는지 정하기 (begin ~ end 까지의 앱 이름을 수집한다)
        val INTERVAL: Long = 10*1000
        val end = System.currentTimeMillis()
        // 1 minute ago
        val begin = end - INTERVAL

        val packageNameMap: LongSparseArray<String>? = null

        // 수집한 이벤트들을 담기 위한 UsageEvents
        val usageEvents = usageStatsManager.queryEvents(begin, end)

        while (usageEvents.hasNextEvent()) {
            val event = UsageEvents.Event()
            usageEvents.getNextEvent(event)
            if (isForeGroundEvent(event)) {
                packageNameMap!!.put(event.timeStamp, event.packageName)
                if (event.timeStamp > lastRunAppTimeStamp) {
                    lastRunAppTimeStamp = event.timeStamp
                }
            }
        }
        return packageNameMap!!.get(lastRunAppTimeStamp, "").toString()
    }

    private fun isForeGroundEvent(event: UsageEvents.Event?): Boolean {
        if (event == null) return false
        return if (BuildConfig.VERSION_CODE >= 29) event.eventType == UsageEvents.Event.ACTIVITY_RESUMED else event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND
    }

    private fun checkPermission(): Boolean {
        var granted: Boolean = false
        val appOps = applicationContext
            .getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), applicationContext.packageName
        )
        granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            applicationContext.checkCallingOrSelfPermission(
                Manifest.permission.PACKAGE_USAGE_STATS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
        return granted
    }
}