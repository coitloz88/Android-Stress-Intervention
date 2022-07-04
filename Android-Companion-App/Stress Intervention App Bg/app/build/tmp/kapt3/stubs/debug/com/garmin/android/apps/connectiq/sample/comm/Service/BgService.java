package com.garmin.android.apps.connectiq.sample.comm.Service;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\fH\u0002J\b\u0010\u0017\u001a\u00020\u0011H\u0002J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u0011H\u0016J\b\u0010\u001d\u001a\u00020\u0011H\u0016J\"\u0010\u001e\u001a\u00020\u000f2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u000fH\u0016J\u0016\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/Service/BgService;", "Landroid/app/Service;", "()V", "DBhelper", "Lcom/garmin/android/apps/connectiq/sample/comm/roomdb/AppDatabase;", "connectIQ", "Lcom/garmin/android/connectiq/ConnectIQ;", "device", "Lcom/garmin/android/connectiq/IQDevice;", "myApp", "Lcom/garmin/android/connectiq/IQApp;", "IBItoHRV", "", "IBI_samples", "", "", "giveFeedback", "", "rawDatas", "", "isLowerHRV", "", "userHRV", "listenByMyAppEvents", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "flags", "startId", "parseSensorData", "Companion", "app_debug"})
public final class BgService extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    public static final com.garmin.android.apps.connectiq.sample.comm.Service.BgService.Companion Companion = null;
    private static final java.lang.String TAG = "BgService";
    private static final java.lang.String EXTRA_IQ_DEVICE = "IQDevice";
    private static final java.lang.String COMM_WATCH_ID = "5d80e574-aa63-4fae-8dc0-f58656071277";
    private final com.garmin.android.connectiq.ConnectIQ connectIQ = null;
    private com.garmin.android.connectiq.IQDevice device;
    private com.garmin.android.connectiq.IQApp myApp;
    private com.garmin.android.apps.connectiq.sample.comm.roomdb.AppDatabase DBhelper;
    
    public BgService() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    private final void listenByMyAppEvents() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final java.util.List<java.lang.Integer> parseSensorData(java.lang.String rawDatas) {
        return null;
    }
    
    private final double IBItoHRV(java.util.List<java.lang.Integer> IBI_samples) {
        return 0.0;
    }
    
    private final boolean isLowerHRV(double userHRV) {
        return false;
    }
    
    private final void giveFeedback(java.lang.String rawDatas) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/Service/BgService$Companion;", "", "()V", "COMM_WATCH_ID", "", "EXTRA_IQ_DEVICE", "TAG", "putIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "device", "Lcom/garmin/android/connectiq/IQDevice;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.content.Intent putIntent(@org.jetbrains.annotations.NotNull()
        android.content.Context context, @org.jetbrains.annotations.Nullable()
        com.garmin.android.connectiq.IQDevice device) {
            return null;
        }
    }
}