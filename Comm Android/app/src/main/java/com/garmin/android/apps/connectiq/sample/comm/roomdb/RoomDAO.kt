package com.garmin.android.apps.connectiq.sample.comm.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.sql.Time

@Dao
interface RoomDAO {
    @Query("SELECT * FROM HRVdata")
    fun getAllHRVdata(): List<HRVdata>

    @Insert
    fun insertHRVdata(curretTime: Time, HRVdataSample: Double)

    @Delete
    fun deleteHRVdata(hrVdata: HRVdata)
}