package com.garmin.android.apps.connectiq.sample.comm.roomdb;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\'J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\nH\'J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'\u00a8\u0006\f"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/RoomDAO3;", "", "delete", "", "locationdata", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/Locationdata;", "findByTime", "currentTime", "", "getAllLocationdata", "", "insert", "app_debug"})
public abstract interface RoomDAO3 {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Locationdata")
    public abstract java.util.List<com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata> getAllLocationdata();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * FROM Locationdata WHERE current_time LIKE :currentTime")
    public abstract com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata findByTime(@org.jetbrains.annotations.NotNull()
    java.lang.String currentTime);
    
    @androidx.room.Insert()
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata locationdata);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    com.garmin.android.apps.connectiq.sample.comm.roomdb.Locationdata locationdata);
}