package com.garmin.android.apps.connectiq.sample.comm.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HRVdata::class], version = 1)
abstract class AppDatabase1 : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO1

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase1? = null

        fun getInstance(context: Context): AppDatabase1 {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase1::class.java, "userdb")
                .build()
    }
}

@Database(entities = [ESMdata::class], version = 1)
abstract class AppDatabase2 : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO2
    companion object {

        @Volatile
        private var instance: AppDatabase2? = null

        fun getInstance(context: Context): AppDatabase2 {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase2::class.java, "userdb")
                .build()
    }
}

@Database(entities = [Locationdata::class], version = 1)
abstract class AppDatabase3 : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO3
    companion object {

        @Volatile
        private var instance: AppDatabase3? = null

        fun getInstance(context: Context): AppDatabase3 {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase3::class.java, "userdb")
                .build()
    }
}

@Database(entities = [Accdata::class], version = 1)
abstract class AppDatabase4 : RoomDatabase() {
    abstract fun roomDAO(): RoomDAO4
    companion object {

        @Volatile
        private var instance: AppDatabase4? = null

        fun getInstance(context: Context): AppDatabase4 {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase4::class.java, "userdb")
                .build()
    }
}