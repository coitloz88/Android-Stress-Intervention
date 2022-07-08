package com.garmin.android.apps.connectiq.sample.comm.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.garmin.android.apps.connectiq.sample.comm.R

class InterventionActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intervention)

        toolbar = findViewById(R.id.intervention_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }
}