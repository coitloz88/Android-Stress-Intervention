package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HRVdata(
    @PrimaryKey val current_time: String,
    @ColumnInfo(name = "HRV_data") val HRVdata: Double
)

@Entity(primaryKeys = ["current_time", "Package_Name"])
data class PhoneUsageData(
    val current_time: String,
    @ColumnInfo(name = "Package_Name") val PackageName: String,
    @ColumnInfo(name = "LastTime_Used") val LastTimeUsed: String,
    @ColumnInfo(name = "TotalTime_Foreground") val TotalTimeInForeground: Long,
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

