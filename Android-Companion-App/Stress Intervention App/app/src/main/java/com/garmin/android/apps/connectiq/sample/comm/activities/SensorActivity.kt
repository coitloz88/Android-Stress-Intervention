package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.garmin.android.apps.connectiq.sample.comm.R
import com.garmin.android.apps.connectiq.sample.comm.SensorFactory
import com.garmin.android.apps.connectiq.sample.comm.adapter.SensorDatasAdapter

private const val TAG = "SensorActivity"

class SensorActivity : Activity() {
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
    }
}