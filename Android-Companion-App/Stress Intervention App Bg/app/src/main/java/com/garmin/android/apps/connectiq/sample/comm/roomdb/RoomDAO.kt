package com.garmin.android.apps.connectiq.sample.comm.roomdb

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

@Dao
interface RoomDAO3 {
    @Query("SELECT * FROM Locationdata")
    fun getAllLocationdata(): List<Locationdata>

    @Query("SELECT * FROM Locationdata WHERE current_time LIKE :currentTime")
    fun findByTime(currentTime: String): Locationdata

    @Insert
    fun insert(locationdata: Locationdata)

    @Delete
    fun delete(locationdata: Locationdata)
}

@Dao
interface RoomDAO4 {
    @Query("SELECT * FROM Accdata")
    fun getAllESMdata(): List<Accdata>

    @Query("SELECT * FROM Accdata WHERE current_time LIKE :currentTime")
    fun findByTime(currentTime: String): Accdata

    @Insert
    fun insert(accdata: Accdata)

    @Delete
    fun delete(accdata: Accdata)
}