package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity
data class HRVdata(
    @PrimaryKey val time: Time,
    @ColumnInfo(name = "HRV_data") val HRVdata: Double
)