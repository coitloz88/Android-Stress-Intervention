package com.garmin.android.apps.connectiq.sample.comm

import android.content.Context

data class SensorData(val text: String, val payload: Any)

object SensorFactory {
    @JvmStatic
    fun getSensorDatas(context: Context) = listOf(
        SensorData(
            context.getString(R.string.start_location_update),
            context.getString(R.string.start_location_update_payload)
        ),
        SensorData(
            context.getString(R.string.stop_location_updates),
            context.getString(R.string.stop_location_updates_payload)
        ),
        /*SensorData(
            context.getString(R.string.start_acc_update),
            context.getString(R.string.start_acc_update_payload)

        ),
        SensorData(
            context.getString(R.string.stop_acc_updates),
            context.getString(R.string.stop_acc_updates_payload)
        )*/
        SensorData(
            context.getString(R.string.start_pu_update),
            context.getString(R.string.start_pu_update_payload)
        ),
        SensorData(
            context.getString(R.string.stop_pu_updates),
            context.getString(R.string.stop_pu_updates_payload)
        )
    )
}