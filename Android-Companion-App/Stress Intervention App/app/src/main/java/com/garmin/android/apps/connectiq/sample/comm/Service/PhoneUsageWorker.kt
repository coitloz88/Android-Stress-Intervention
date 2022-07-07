package com.garmin.android.apps.connectiq.sample.comm.Service

import android.R
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase2
import com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata
import java.sql.Timestamp
import java.util.*

private val TAG = "PhoneUsageWorker"

class PhoneUsageWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams){

    override fun doWork(): Result {
        return try {
            val id: Int = 3

            val name = "Phone usage service"

            val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT

                // Add the channel
                val channel = NotificationChannel("phone_usage_channel", name, importance)
                notificationManager.createNotificationChannel(channel)
            }
            // Create the notification
            val builder = NotificationCompat.Builder(applicationContext, "phone_usage_channel")
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentTitle("PhoneUsage Service")

            NotificationManagerCompat.from(applicationContext).notify(id, builder.build())

            //TODO
            //usage stats 가져오기



            // DB 백업
            val addRunnable = Runnable {
                AppDatabase2.getInstance(applicationContext).roomDAO().insert())
            }
            val thread = Thread(addRunnable)
            thread.start()


            Result.success()
        } catch (exception: Exception){
                exception.printStackTrace()
                Result.failure()
            }
    }

    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == MODE_ALLOWED
    }

    private fun showAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        usageStats.forEach { it ->
            Log.d(TAG, "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                    "totalTimeInForeground: ${it.totalTimeInForeground}")
        }
    }

    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)

        val usageStatsManager = getSystemService(applicationContext) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }


}