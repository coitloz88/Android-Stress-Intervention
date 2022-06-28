package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDAO {
    @Query("SELECT * FROM HRVdata")
    fun getAllHRVdata(): List<HRVdata>

    @Query("SELECT * FROM HRVdata WHERE current_time LIKE :currentTime")
    fun findByTime(currentTime: String): HRVdata

    @Insert
    fun insert(hrvdata: HRVdata)

    @Delete
    fun delete(hrvdata: HRVdata)

    @Query("DELETE FROM HRVdata")
    fun deleteAll()
}