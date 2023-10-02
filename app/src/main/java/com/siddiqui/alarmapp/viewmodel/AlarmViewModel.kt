package com.siddiqui.alarmapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.siddiqui.alarmapp.repo.AlarmRepository

class AlarmViewModel(application: Application):AndroidViewModel(application) {
    private val repository = AlarmRepository(application)

    // Function to schedule an alarm
    fun scheduleAlarm(alarmTimeMillis: Long,dateTime:String) {
        repository.scheduleAlarm(alarmTimeMillis, dateTime)
    }
    // Function to show a notification

}