package com.siddiqui.alarmapp.alarmBroadcast

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.siddiqui.alarmapp.repo.AlarmRepository
import com.siddiqui.alarmapp.repo.MediaPlayerManager

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null) {
                val application = context.applicationContext as Application
                val alarmRepository = AlarmRepository(application)
                alarmRepository.showNotification("Schedule Alarm")
                val mediaPlayerManager = MediaPlayerManager.getInstance(context)
                mediaPlayerManager.start()
            }
    }

}