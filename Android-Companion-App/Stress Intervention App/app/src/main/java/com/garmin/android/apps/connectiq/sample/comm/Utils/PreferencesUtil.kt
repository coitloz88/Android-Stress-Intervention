package com.garmin.android.apps.connectiq.sample.comm.Utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getBoolean(key:String, value:Boolean):Boolean{
        return prefs.getBoolean(key, value)
    }

    fun setBoolean(key:String, value:Boolean){
        prefs.edit().putBoolean(key,value).apply()
    }
}