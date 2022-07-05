package com.garmin.android.apps.connectiq.sample.comm.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0016J\u0018\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00022\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/garmin/android/apps/connectiq/sample/comm/adapter/IQDeviceAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/garmin/android/connectiq/IQDevice;", "Lcom/garmin/android/apps/connectiq/sample/comm/adapter/IQDeviceViewHolder;", "onItemClickListener", "Lkotlin/Function1;", "", "(Lkotlin/jvm/functions/Function1;)V", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateDeviceStatus", "device", "status", "Lcom/garmin/android/connectiq/IQDevice$IQDeviceStatus;", "app_debug"})
public final class IQDeviceAdapter extends androidx.recyclerview.widget.ListAdapter<com.garmin.android.connectiq.IQDevice, com.garmin.android.apps.connectiq.sample.comm.adapter.IQDeviceViewHolder> {
    private final kotlin.jvm.functions.Function1<com.garmin.android.connectiq.IQDevice, kotlin.Unit> onItemClickListener = null;
    
    public IQDeviceAdapter(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.garmin.android.connectiq.IQDevice, kotlin.Unit> onItemClickListener) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.garmin.android.apps.connectiq.sample.comm.adapter.IQDeviceViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.garmin.android.apps.connectiq.sample.comm.adapter.IQDeviceViewHolder holder, int position) {
    }
    
    public final void updateDeviceStatus(@org.jetbrains.annotations.NotNull()
    com.garmin.android.connectiq.IQDevice device, @org.jetbrains.annotations.Nullable()
    com.garmin.android.connectiq.IQDevice.IQDeviceStatus status) {
    }
}