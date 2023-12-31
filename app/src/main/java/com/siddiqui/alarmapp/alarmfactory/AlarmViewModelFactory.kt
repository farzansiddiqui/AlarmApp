package com.siddiqui.alarmapp.alarmfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.siddiqui.alarmapp.viewmodel.AlarmViewModel
import java.lang.IllegalArgumentException

class AlarmViewModelFactory(private val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)){
            return AlarmViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}