package com.garmin.android.apps.connectiq.sample.comm.mobilephonedata

import android.Manifest
import android.R
import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.util.Log
import android.util.LongSparseArray
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.garmin.android.apps.connectiq.sample.comm.BuildConfig

class PhoneUsageService : Service() {
    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun startPUService() {
        Log.d("checkpoint","start")
        val channelId = "phone_usage_notification_channel"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.mipmap.sym_def_app_icon)
        builder.setContentTitle("Phone Usage Service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(false)
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

    private fun stopPUService() {
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == Constants.ACTION_START_PU_SERVICE) {
                    Log.d("checkpoint","start")
                    startPUService()
                } else if (action == Constants.ACTION_STOP_PU_SERVICE) {
                    stopPUService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
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