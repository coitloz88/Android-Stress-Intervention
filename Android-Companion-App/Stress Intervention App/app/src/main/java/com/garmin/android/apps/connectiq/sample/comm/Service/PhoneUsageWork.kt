package com.garmin.android.apps.connectiq.sample.comm.Service

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import kotlin.Comparator

private const val TAG = "PhoneUsageWorker"

class PhoneUsageWork(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try{
            val usageStats = getAppUsageStats()
            showAppUsageStats(usageStats)
            Result.success()
        } catch(exception: Exception){
            Result.failure()
        }
    }


    private fun showAppUsageStats(usageStats: MutableList<UsageStats>) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        val lastUsageStats = usageStats[0]
        Log.d(TAG, "packageName: ${lastUsageStats.packageName}, lastTimeUsed: ${Date(lastUsageStats.lastTimeUsed)}}," + "totalTimeInForeground: ${lastUsageStats.totalTimeInForeground}")
        val addRunnable = Runnable {
            AppDatabase.getInstance(applicationContext).roomDAO().insertPhoneUsageData(Timestamp(System.currentTimeMillis()).toString(), lastUsageStats.packageName.toString(), Date(lastUsageStats.lastTimeUsed).toString(), lastUsageStats.totalTimeInForeground)
        }
        val thread = Thread(addRunnable)
        thread.start()

        /*
        usageStats.forEach { it ->
            if(it.totalTimeInForeground != 0.toLong()){ //foreground에서 사용된 시간이 0인 앱은 저장하지 않음
                Log.d(
                    TAG, "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                        "totalTimeInForeground: ${it.totalTimeInForeground}")

                val addRunnable = Runnable {
                    AppDatabase.getInstance(applicationContext).roomDAO().insertPhoneUsageData(Timestamp(System.currentTimeMillis()).toString(), it.packageName.toString(), Date(it.lastTimeUsed).toString(), it.totalTimeInForeground)
                }
                val thread = Thread(addRunnable)
                thread.start()
            }
        }
        */
    }

    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)

        val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }

}