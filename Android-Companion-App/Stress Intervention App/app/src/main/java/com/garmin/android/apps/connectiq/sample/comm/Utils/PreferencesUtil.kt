package com.garmin.android.apps.connectiq.sample.comm.Utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtil(context: Context) {
    //isIntervention: 현재 intervention 진행중 여부
    //isConnected: 현재 기기가 연결되어있는지 여부
    
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, value:String): String{
        return prefs.getString(key, value).toString()
    }

    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getBoolean(key:String, value:Boolean):Boolean{
        return prefs.getBoolean(key, value)
    }

    fun setBoolean(key:String, value:Boolean){
        prefs.edit().putBoolean(key,value).apply()
    }
}