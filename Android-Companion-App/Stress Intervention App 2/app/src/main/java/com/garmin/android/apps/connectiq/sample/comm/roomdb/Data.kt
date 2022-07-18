package com.garmin.android.apps.connectiq.sample.comm.roomdb

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

@Entity
data class Locationdata(
    @PrimaryKey val current_time: String,
    @ColumnInfo(name = "Latitude_data") val Latitudedata: Double,
    @ColumnInfo(name = "Longtitude_data") val Longtitudedata: Double
)

@Entity
data class Accdata(
    @PrimaryKey val current_time: String,
    @ColumnInfo(name = "Acc_X_data") val AccXdata: Float,
    @ColumnInfo(name = "Acc_Y_data") val AccYdata: Float,
    @ColumnInfo(name = "Acc_Z_data") val AccZdata: Float
)