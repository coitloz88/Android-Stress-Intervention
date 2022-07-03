package com2.garmin.android.apps.connectiq.sample.comm2.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDAO1 {
    @Query("SELECT * FROM HRVdata")
    fun getAllHRVdata(): List<HRVdata>

    @Query("SELECT * FROM HRVdata WHERE current_time LIKE :currentTime")
    fun findByTime(currentTime: String): HRVdata

    @Insert
    fun insert(hrvdata: HRVdata)

    @Delete
    fun delete(hrvdata: HRVdata)
}

@Dao
interface RoomDAO2 {
    @Query("SELECT * FROM ESMdata")
    fun getAllESMdata(): List<ESMdata>

    @Query("SELECT * FROM ESMdata WHERE current_time LIKE :currentTime")
    fun findByTime(currentTime: String): ESMdata

    @Insert
    fun insert(esmdata: ESMdata)

    @Delete
    fun delete(esmdata: ESMdata)
}