package com.siddiqui.alarmapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siddiqui.alarmapp.R
import com.siddiqui.alarmapp.model.TimerModel
import com.siddiqui.alarmapp.viewmodel.RecyclerViewModel

class AlarmAdapter(val viewModel: RecyclerViewModel,val arrayList: ArrayList<TimerModel>,context: Context): RecyclerView.Adapter<AlarmAdapter.MyCustomViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.MyCustomViewModel {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_layout,parent,false)
        return MyCustomViewModel(root)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyCustomViewModel, position: Int) {
        holder.bind(arrayList[position])
    }
    inner class MyCustomViewModel(private val binding: View): RecyclerView.ViewHolder(binding){
        val title = binding.findViewById<TextView>(R.id.title)
        val imgBtn = binding.findViewById<ImageView>(R.id.imgBtn)
        fun bind(timerModel: TimerModel) {
            title.text = timerModel.timerData
            imgBtn.setOnClickListener {
                viewModel.remove(timerModel)
                notifyItemRemoved(arrayList.indexOf(timerModel))
            }
        }
    }
}