package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HRVdata::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO
}