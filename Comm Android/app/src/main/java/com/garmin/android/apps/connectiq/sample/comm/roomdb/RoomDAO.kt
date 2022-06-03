package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.sql.Timestamp

@Dao
interface RoomDAO {
    @Query("SELECT * FROM HRVdata")
    fun getAllHRVdata(): List<HRVdata>

    @Insert
    fun insert(currentTime: Timestamp , hrvdata: Double)

    @Delete
    fun delete(hrvdata: HRVdata)
}