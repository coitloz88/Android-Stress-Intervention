package com.garmin.android.apps.connectiq.sample.comm.mobilephonedata;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001a\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0017J\u0012\u0010\u001a\u001a\u00020\u00112\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J \u0010\u001d\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u0015H\u0016J\b\u0010 \u001a\u00020\u0011H\u0002J\b\u0010!\u001a\u00020\u0011H\u0002R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r\u00a8\u0006\""}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/mobilephonedata/AccService;", "Landroid/app/Service;", "Landroid/hardware/SensorEventListener;", "()V", "DBhelper", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase4;", "getDBhelper", "()Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase4;", "setDBhelper", "(Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase4;)V", "sensorManager", "Landroid/hardware/SensorManager;", "getSensorManager", "()Landroid/hardware/SensorManager;", "sensorManager$delegate", "Lkotlin/Lazy;", "onAccuracyChanged", "", "p0", "Landroid/hardware/Sensor;", "p1", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "onStartCommand", "flags", "startId", "startAccService", "stopAccService", "app_debug"})
public final class AccService extends android.app.Service implements android.hardware.SensorEventListener {
    @org.jetbrains.annotations.Nullable()
    private com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase4 DBhelper;
    private final kotlin.Lazy sensorManager$delegate = null;
    
    public AccService() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase4 getDBhelper() {
        return null;
    }
    
    public final void setDBhelper(@org.jetbrains.annotations.Nullable()
    com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase4 p0) {
    }
    
    private final android.hardware.SensorManager getSensorManager() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    @androidx.annotation.Nullable()
    @java.lang.Override()
    public android.os.IBinder onBind(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent) {
        return null;
    }
    
    private final void startAccService() {
    }
    
    private final void stopAccService() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.NotNull()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onSensorChanged(@org.jetbrains.annotations.Nullable()
    android.hardware.SensorEvent event) {
    }
    
    @java.lang.Override()
    public void onAccuracyChanged(@org.jetbrains.annotations.Nullable()
    android.hardware.Sensor p0, int p1) {
    }
}