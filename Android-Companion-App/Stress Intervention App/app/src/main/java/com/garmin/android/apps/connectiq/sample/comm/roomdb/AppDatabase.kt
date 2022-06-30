package com.garmin.android.apps.connectiq.sample.comm.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HRVdata::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "userdb")
                .build()
    }
}