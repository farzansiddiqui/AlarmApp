package com.siddiqui.alarmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siddiqui.alarmapp.model.TimerModel

class RecyclerViewModel:ViewModel() {
    var lst = MutableLiveData<ArrayList<TimerModel>>()
    private var newList = arrayListOf<TimerModel>()

    fun addBlog(timerModel: TimerModel){
        newList.add(timerModel)
        lst.value = newList
    }
    fun remove(timerModel: TimerModel){
        newList.remove(timerModel)
        lst.value = newList
    }

}