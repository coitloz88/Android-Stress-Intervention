/**
 * Copyright (C) 2015 Garmin International Ltd.
 * Subject to Garmin SDK License Agreement and Wearables Application Developer Agreement.
 */
package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.Service.InterventionService
import com.garmin.android.apps.connectiq.sample.comm.Utils.mPreferences
import com.garmin.android.apps.connectiq.sample.comm.adapter.IQDeviceAdapter
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQDevice
import com.garmin.android.connectiq.exception.InvalidStateException
import com.garmin.android.connectiq.exception.ServiceUnavailableException


class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var connectIQ: ConnectIQ
    private lateinit var adapter: IQDeviceAdapter
    private lateinit var btnIntervention: Button
    private var isSdkReady = false

    private lateinit var toolbar: Toolbar

    private val connectIQListener: ConnectIQ.ConnectIQListener =
        object : ConnectIQ.ConnectIQListener {
            override fun onInitializeError(errStatus: ConnectIQ.IQSdkErrorStatus) {
                setEmptyState(getString(R.string.initialization_error) + ": " + errStatus.name)
                isSdkReady = false
            }

            override fun onSdkReady() {
                loadDevices()
                isSdkReady = true
            }

            override fun onSdkShutDown() {
                isSdkReady = false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        setupUi()
        setupConnectIQSdk()

        btnIntervention = findViewById(R.id.btn_intervention)

        btnIntervention.setOnClickListener{
            if(isMyServiceRunning(InterventionService::class.java)){
                //현재 intervention이 실행중인 경우, 실행중인 intervention을 종료
                Toast.makeText(applicationContext, "Quit intervention", Toast.LENGTH_SHORT).show()

                val stopIntent = Intent(this, InterventionService::class.java)
                stopService(stopIntent)
                Log.d(TAG, "Quit intervention process")
                //connectIQ.shutdown(this)
            }
            else {
                //intervention이 실행중이지 않은 경우 Toast 메시지를 출력
                Toast.makeText(applicationContext, "No intervention is running", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onResume() {
        super.onResume()

        if (isSdkReady) {
            loadDevices()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun releaseConnectIQSdk() {
        try {
            // It is a good idea to unregister everything and shut things down to
            // release resources and prevent unwanted callbacks.
            connectIQ.unregisterAllForEvents()
            connectIQ.shutdown(this)
        } catch (e: InvalidStateException) {
            // This is usually because the SDK was already shut down
            // so no worries.
        }
    }

    private fun setupUi() {
        // Setup UI.
        adapter = IQDeviceAdapter { onItemClick(it) }
        findViewById<RecyclerView>(R.id.main_recycler_view).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }
    }

    private fun onItemClick(device: IQDevice) {
        if(!isMyServiceRunning(InterventionService::class.java) && mPreferences.prefs.getString("isConnected", "NOT CONNECTED").equals("CONNECTED")){
            Toast.makeText(applicationContext, "Starting Intervention...", Toast.LENGTH_SHORT).show()
            startService(InterventionService.putIntent(this, device))
        } else {
            Toast.makeText(applicationContext, "Intervention cannot start", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "cannot start the intervention")
        }
    }

    private fun setupConnectIQSdk() {
        // Here we are specifying that we want to use a WIRELESS bluetooth connection.
        // We could have just called getInstance() which would by default create a version
        // for WIRELESS, unless we had previously gotten an instance passing TETHERED
        // as the connection type.
        connectIQ = ConnectIQ.getInstance(this, ConnectIQ.IQConnectType.WIRELESS)

        // Initialize the SDK
        connectIQ.initialize(this, true, connectIQListener)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.load_devices -> {
                loadDevices()
                true
            }
            R.id.control_data_collection -> {
                startActivity(Intent(this, SensorActivity::class.java))
                true
            }
            R.id.intervention_ui -> {
                startActivity(Intent(this, InterventionActivity::class.java))
                true
            }
            R.id.view_my_data -> {
                alert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun loadDevices() {
        try {
            // Retrieve the list of known devices.
            val devices = connectIQ.knownDevices ?: listOf()
            // OR You can use getConnectedDevices to retrieve the list of connected devices only.
            // val devices = connectIQ.connectedDevices ?: listOf()

            // Get the connectivity status for each device for initial state.
            devices.forEach {
                it.status = connectIQ.getDeviceStatus(it)
            }

            // Update ui list with the devices data
            adapter.submitList(devices)

            // Let's register for device status updates.
            devices.forEach {
                connectIQ.registerForDeviceEvents(it) { device, status ->
                    adapter.updateDeviceStatus(device, status)
                }
            }
        } catch (exception: InvalidStateException) {
            // This generally means you forgot to call initialize(), but since
            // we are in the callback for initialize(), this should never happen
        } catch (exception: ServiceUnavailableException) {
            // This will happen if for some reason your app was not able to connect
            // to the ConnectIQ service running within Garmin Connect Mobile.  This
            // could be because Garmin Connect Mobile is not installed or needs to
            // be upgraded.
            setEmptyState(getString(R.string.service_unavailable))
        }
    }

    private fun setEmptyState(text: String) {
        findViewById<TextView>(android.R.id.empty)?.text = text
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

    private fun alert() {
        val alertBuilder = AlertDialog.Builder(this@MainActivity)
        val arrayEMA = arrayOf("Very slightly or nor at all", "A little", "Moderately", "Quite a bit", "Extremely")
        alertBuilder.setTitle("Stressed")
        alertBuilder.setSingleChoiceItems(
            arrayEMA, -1,
        ) { dialog, which ->
            try {
                dialog.dismiss()
            } catch (e:IllegalArgumentException) {
                Toast.makeText(this, "Please select your answer", Toast.LENGTH_SHORT).show()
            }
        }
        alertBuilder.create()
        alertBuilder.show()
    }

}