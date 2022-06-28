package com.garmin.android.apps.connectiq.sample.comm

import androidx.work.Worker

class BackgroundWork : Worker() {
    override fun doWork(): Result {
        //TODO: 처리해야할 작업 코드 작성

        return Result.success()
    }
}