package com.garmin.android.apps.connectiq.sample.comm.mobilephonedata;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000eH\u0017J \u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0014H\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/mobilephonedata/LocationService;", "Landroid/app/Service;", "()V", "DBhelper", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase3;", "getDBhelper", "()Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase3;", "setDBhelper", "(Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase3;)V", "mLocationCallback", "Lcom/google/android/gms/location/LocationCallback;", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onStartCommand", "", "flags", "startId", "startLocationService", "", "stopLocationService", "app_debug"})
public final class LocationService extends android.app.Service {
    @org.jetbrains.annotations.Nullable()
    private com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase3 DBhelper;
    private final com.google.android.gms.location.LocationCallback mLocationCallback = null;
    
    public LocationService() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase3 getDBhelper() {
        return null;
    }
    
    public final void setDBhelper(@org.jetbrains.annotations.Nullable()
    com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase3 p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @androidx.annotation.Nullable()
    @java.lang.Override()
    public android.os.IBinder onBind(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
        return null;
    }
    
    private final void startLocationService() {
    }
    
    private final void stopLocationService() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
}