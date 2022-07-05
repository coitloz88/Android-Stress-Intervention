/**
 * Copyright (C) 2015 Garmin International Ltd.
 * Subject to Garmin SDK License Agreement and Wearables Application Developer Agreement.
 */
package com.garmin.android.apps.connectiq.sample.comm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.garmin.android.apps.connectiq.sample.comm.SensorData

class SensorDatasAdapter(
    private val onItemClickListener: (Any) -> Unit
) : ListAdapter<SensorData, SensorDataViewHolder>(SensorDataItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return SensorDataViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SensorDataViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

private class SensorDataItemDiffCallback : DiffUtil.ItemCallback<SensorData>() {
    override fun areItemsTheSame(oldItem: SensorData, newItem: SensorData): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: SensorData, newItem: SensorData): Boolean =
        oldItem == newItem
}

class SensorDataViewHolder(
    private val view: View,
    private val onItemClickListener: (Any) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun bindTo(sensorDatas: SensorData) {
        view.findViewById<TextView>(android.R.id.text1).text = sensorDatas.text
        view.setOnClickListener {
            //TODO: 클릭 시 실행할 것
            onItemClickListener(sensorDatas.payload)
        }
    }
}