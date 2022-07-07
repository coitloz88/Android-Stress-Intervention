package com.garmin.android.apps.connectiq.sample.comm.activities

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.job.JobInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.SensorFactory
import com.garmin.android.apps.connectiq.sample.comm.Service.AccelService
import com.garmin.android.apps.connectiq.sample.comm.adapter.SensorDatasAdapter
import com.garmin.android.apps.connectiq.sample.comm.Service.LocationService
import com.garmin.android.apps.connectiq.sample.comm.Service.PhoneUsageWorker
import java.util.concurrent.TimeUnit

private const val TAG = "SensorActivity"

class SensorActivity : Activity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 1
    private val REQUEST_CODE_PU_PERMISSION = 2
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
    }

    override fun onResume() {
        super.onResume()
        buildSensorDataList()
    }

    private fun buildSensorDataList(){
        val adapter = SensorDatasAdapter { onItemClick(it) }
        adapter.submitList(SensorFactory.getSensorDatas(this@SensorActivity))
        findViewById<RecyclerView>(android.R.id.list).apply {
            layoutManager = LinearLayoutManager(this@SensorActivity)
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
//            if(isMyServiceRunning(PhoneUsageService::class.java)){
//                Log.e(TAG, "Phone Usage Service is already running")
//            } else {
//                startService(Intent(this, PhoneUsageService::class.java))
//            }
            Toast.makeText(applicationContext, "Start Phone Usage Service..", Toast.LENGTH_SHORT).show()

            val uploadWorkRequest: PeriodicWorkRequest = PeriodicWorkRequestBuilder<PhoneUsageWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                ).build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("Phone_Usage_updates_work", ExistingPeriodicWorkPolicy.KEEP, uploadWorkRequest)

        } else if(datas.toString().equals(getString(R.string.stop_pu_updates))){
//            if (isMyServiceRunning(LocationService::class.java)) {
//                Log.d(TAG, "phone usage service 중지")
//                Toast.makeText(applicationContext, "Stop phone usage Service", Toast.LENGTH_SHORT).show()
//                stopService(Intent(this, PhoneUsageService::class.java))
//            } else {
//                Log.e(TAG, "Phone Usage Service is not running")
//            }
            Toast.makeText(applicationContext, "Stop Phone Usage", Toast.LENGTH_SHORT).show()
            WorkManager.getInstance(this).cancelUniqueWork("Phone_Usage_updates_work")
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

    private fun checkPermission(): Boolean {
        var granted = false
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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}