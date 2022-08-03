/**
 * Copyright (C) 2015 Garmin International Ltd.
 * Subject to Garmin SDK License Agreement and Wearables Application Developer Agreement.
 */
package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var btnParse: Button
    private var isSdkReady = false

    private lateinit var toolbar: Toolbar

    private lateinit var rawDatas: String
    private var dataMap1: MutableMap<String, MutableList<Int>> = mutableMapOf()
    private var dataMap2: MutableMap<String, Int> = mutableMapOf()

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
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        setupUi()
        setupConnectIQSdk()

        btnParse = findViewById(R.id.parsing)
        btnParse.setOnClickListener{
            rawDatas = "{30s=[11], 30d=[8], 30x=[-191, -190, -292, -237, -278, -233, -209, -147, -36, 40, 74, 193, 234, 115, 18, -7, 9, 0, 0, 0, -2, -2, -1, -3, -3, -3, 0, -3, -3, 0, 1, 0, -1, 0, 0, 0, 0, 0, 0], 30y=[573, 566, 572, 570, 571, 570, 574, 571, 575, 572, 574, 573, 574, 573, 571, 575, 570, 574, 573, 576, 569, 572, 574, 573, 574, 572, 567, 540, 630, 628, 614, 563, 796, 815, 621, 540, 411, 281, 389, 437, 449, 432], 30i=[865, 915, 924, 921, 905, 860, 843, 884, 853, 857, 864, 865, 882, 900, 916, 922, 931, 954], 30z=[-861, -862, -861, -862, -861, -861, -861, -860, -864, -863, -861, -862, -1031, -901, -864, -868, -857, -855, -860, -854, -859, -860]}"
            var dataName: String = String()
            var dataList = rawDatas.split("=", "], ") // ],로 하면 안됨
            for (i in dataList)
                Log.d(TAG, "$i")
            dataList.forEach{
                if (it.contains("i" ) || it.contains("x") || it.contains("y") || it.contains("z") || it.contains("s") || it.contains("d")) {
                    dataName = it.last().toString()
                    //return@forEach
                }
                else {
                    if (it.contains(",")) {
                        if (it.contains("]")) {
                            dataMap1.put(
                                dataName,
                                it.substring(it.indexOf("[") + 1, it.indexOf("]")).replace(" ", "").split(",").map{it.toInt()} as MutableList<Int>)
                        }
                        else{
                            dataMap1.put(
                                dataName,
                                it.substring(it.indexOf("[") + 1).replace(" ", "").split(",").map{it.toInt()} as MutableList<Int>)
                        }
                    }
                    else{
                        Log.d(TAG, "$it")
                        if(it.contains("]")) {
                            dataMap2.put(
                                dataName,
                                it.substring(it.indexOf("[") + 1, it.indexOf("]")).toInt())
                        }
                        else {
                            dataMap2.put(
                                dataName,
                                it.substring(it.indexOf(("[")) + 1).toInt())
                        }
                    }
                }
            }
            Log.d(TAG, "$dataMap1")
            Log.d(TAG, "$dataMap2")
        }

        btnIntervention = findViewById(R.id.btn_control_data_collection)

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
        if(!isMyServiceRunning(InterventionService::class.java)){
            Toast.makeText(applicationContext, "Starting Intervention...", Toast.LENGTH_SHORT).show()
            startService(InterventionService.putIntent(this, device))
        } else {
            Toast.makeText(applicationContext, "Intervention cannot start", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "cannot start the intervention")
        }
        /*
        if(!isMyServiceRunning(InterventionService::class.java) && mPreferences.prefs.getString("isConnected", "NOT CONNECTED").equals("CONNECTED")){
            Toast.makeText(applicationContext, "Starting Intervention...", Toast.LENGTH_SHORT).show()
            startService(InterventionService.putIntent(this, device))
        } else {
            Toast.makeText(applicationContext, "Intervention cannot start", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "cannot start the intervention")
        }
        */
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
            R.id.intervention2_ui -> {
                startActivity(Intent(this, InterventionActivity2::class.java))
                true
            }
            R.id.esm_ui -> {
                startActivity(Intent(this, ESMActivity::class.java))
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


}