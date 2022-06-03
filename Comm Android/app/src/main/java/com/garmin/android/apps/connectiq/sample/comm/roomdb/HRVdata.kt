package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class HRVdata(
    @PrimaryKey val current_time: Timestamp,
    @ColumnInfo(name = "HRV_data") val HRVdata: Double
)