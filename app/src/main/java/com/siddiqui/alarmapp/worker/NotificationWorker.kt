package com.siddiqui.alarmapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.siddiqui.alarmapp.R

class NotificationWorker(context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {

        val message = inputData.getString(INPUT_MESSAGE)
        Log.d("TAG", "doWork: $message")
        showNotification(message ?: "")
        return Result.success()
    }
    private fun showNotification(message: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                INPUT_ALARM_TIME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Alarm Alert!!")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

    }


    companion object {
        const val INPUT_ALARM_TIME = "alarm_time"
        const val INPUT_MESSAGE = "message"
        const val CHANNEL_ID = "alarm_channel"
        const val NOTIFICATION_ID = 123

        // Function to create input data for the worker
        fun createInputData(message: String): Data {
            return Data.Builder()
                .putString(INPUT_MESSAGE, message)
                .build()
        }
    }
}