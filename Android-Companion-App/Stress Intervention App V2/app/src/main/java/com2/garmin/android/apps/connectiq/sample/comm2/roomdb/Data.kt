package com2.garmin.android.apps.connectiq.sample.comm2.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HRVdata(
    @PrimaryKey val current_time: String,
    @ColumnInfo(name = "HRV_data") val HRVdata: Double
)

@Entity
data class ESMdata(
    @PrimaryKey val current_time: String,
    @ColumnInfo(name = "ESM_data") val ESMdata: String
)