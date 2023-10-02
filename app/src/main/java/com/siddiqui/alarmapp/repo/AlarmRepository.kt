package com.siddiqui.alarmapp.repo
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.siddiqui.alarmapp.alarmBroadcast.AlarmReceiver
import com.siddiqui.alarmapp.worker.NotificationWorker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlarmRepository(application: Application) {
    private val context: Context = application.applicationContext

    fun scheduleAlarm(dateTime: String){
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val date = sdf.parse(dateTime)

        val calendar = Calendar.getInstance()

        date?.let { calendar.time = it }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (e: SecurityException) {
            Log.d("TAG", "scheduleAlarm: ${e.printStackTrace()}")
        }
    }


    // Function to show a notification using WorkManager
    fun showNotification(message: String) {
        val inputData = NotificationWorker.createInputData(message)
        val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(notificationRequest)
    }
}