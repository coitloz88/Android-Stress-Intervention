package com.garmin.android.apps.connectiq.sample.comm.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.SensorFactory
import com.garmin.android.apps.connectiq.sample.comm.Service.AccelService
import com.garmin.android.apps.connectiq.sample.comm.adapter.SensorDatasAdapter
import com.garmin.android.apps.connectiq.sample.comm.Service.LocationService
import com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase
import com.garmin.android.apps.connectiq.sample.comm.roomdb.PhoneUsageData
import java.sql.Timestamp
import java.util.*
import kotlin.Comparator

private const val TAG = "SensorActivity"

class SensorActivity : AppCompatActivity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 1

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        toolbar = findViewById(R.id.sensor_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        buildSensorDataList()
    }

    private fun buildSensorDataList(){
        val adapter = SensorDatasAdapter { onItemClick(it) }
        adapter.submitList(SensorFactory.getSensorDatas(this@SensorActivity))
        findViewById<RecyclerView>(android.R.id.list).apply {
            layoutManager = GridLayoutManager(this@SensorActivity, 2)
            this.adapter = adapter
        }
    }

    private fun onItemClick(datas: Any){
        //TODO: 각 아이템 클릭 시 실행할 것
        Log.d(TAG, datas.toString())

        //location service
        if(datas.toString().equals(getString(R.string.start_location_update))){
            Log.d(TAG, "location service 시작")
            if(ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this@SensorActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE_LOCATION_PERMISSION
                )
            } else {
                if (isMyServiceRunning(LocationService::class.java)){
                    Log.e(TAG, "Location Service is already running")
                } else {
                    Toast.makeText(applicationContext, "Start Location Service..", Toast.LENGTH_SHORT).show()
                    startService(Intent(this, LocationService::class.java))
                }
            }
        } else if(datas.toString().equals(getString(R.string.stop_location_updates))){
            if (isMyServiceRunning(LocationService::class.java)) {
                Log.d(TAG, "location service 중지")
                Toast.makeText(applicationContext, "Stop Location Service", Toast.LENGTH_SHORT).show()
                stopService(Intent(this, LocationService::class.java))
            } else {
                Log.e(TAG, "Location Service is not running")
            }
        }

        // Phone usage service
            //TODO: Not implemented yet
        else if(datas.toString().equals(getString(R.string.start_pu_update))){
            if(!checkForPermission()){
                Log.i(TAG, "The user may not allow the access to apps usage. ")
                Toast.makeText(
                    this,
                    "Failed to retrieve app usage statistics. " +
                            "You may need to enable access for this app through " +
                            "Settings > Security > Apps with usage access",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            } else {
                val usageStats = getAppUsageStats()
                showAppUsageStats(usageStats)
            }
        } else if(datas.toString().equals(getString(R.string.stop_pu_updates))){

        }

        else if(datas.toString().equals(getString(R.string.start_acc_update))){
            if(isMyServiceRunning(AccelService::class.java)){
                Log.e(TAG, "Acc Service is already running")
            } else {
                Toast.makeText(applicationContext, "Start AccService..", Toast.LENGTH_SHORT).show()
                startService(Intent(this, AccelService::class.java))
            }
        } else if(datas.toString().equals(getString(R.string.stop_acc_updates))){
            if (isMyServiceRunning(AccelService::class.java)) {
                Log.d(TAG, "acc service 중지")
                Toast.makeText(applicationContext, "Stop Acc Service", Toast.LENGTH_SHORT).show()
                stopService(Intent(this, AccelService::class.java))
            } else {
                Log.e(TAG, "acc Service is not running")
            }
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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
            if(it.totalTimeInForeground != 0.toLong()){ //foreground에서 사용된 시간이 0인 앱은 저장하지 않음
                Log.d(TAG, "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                        "totalTimeInForeground: ${it.totalTimeInForeground}")

                val addRunnable = Runnable {
                    AppDatabase.getInstance(this).roomDAO().insertPhoneUsageData(Timestamp(System.currentTimeMillis()).toString(), it.packageName.toString(), Date(it.lastTimeUsed).toString(), it.totalTimeInForeground)
                }
                val thread = Thread(addRunnable)
                thread.start()
            }
        }
    }

    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }
}