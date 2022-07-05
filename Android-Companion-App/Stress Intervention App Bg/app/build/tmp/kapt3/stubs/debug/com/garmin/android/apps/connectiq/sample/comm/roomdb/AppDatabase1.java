package com.garmin.android.apps.connectiq.sample.comm.roomdb;

import java.lang.System;

@androidx.room.Database(entities = {com.garmin.android.apps.connectiq.sample.comm.roomdb.HRVdata.class}, version = 1)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase1;", "Landroidx/room/RoomDatabase;", "()V", "roomDAO", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/RoomDAO1;", "Companion", "app_debug"})
public abstract class AppDatabase1 extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    public static final com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1.Companion Companion = null;
    @kotlin.jvm.Volatile()
    private static volatile com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1 instance;
    
    public AppDatabase1() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.garmin.android.apps.connectiq.sample.comm.roomdb.RoomDAO1 roomDAO();
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase1$Companion;", "", "()V", "instance", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase1;", "buildDatabase", "context", "Landroid/content/Context;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1 getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
        
        private final com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase1 buildDatabase(android.content.Context context) {
            return null;
        }
    }
}