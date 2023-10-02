package com.siddiqui.alarmapp.view
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.siddiqui.alarmapp.R
import com.siddiqui.alarmapp.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AlarmBottomSheet:BottomSheetDialogFragment() {
    private lateinit var view: View
    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.btm_sheet_dialog, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateTextView = view.findViewById<TextView>(R.id.setDayOrDate)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val closetBtmImageView = view.findViewById<ImageView>(R.id.close_btmSheet)

        closetBtmImageView.setOnClickListener {
            dismiss()
        }
        dateTextView.text = getTodayDate()


        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val btn = view.findViewById<MaterialButton>(R.id.setTimer)
        btn.text = getTenMinutesLaterTimeInIST24Format()

        val dateChip = view.findViewById<Chip>(R.id.dateChip)
        dateChip.text = getTodayDate()

        val timerChip = view.findViewById<Chip>(R.id.chipTimer)
        timerChip.text = getTenMinutesLaterTimeInIST24Format()

        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val date = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
            val formattedDate =
                date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
            dateTextView.text = formattedDate
            dateChip.text = formattedDate
        }

        timePicker.setOnTimeChangedListener { timePicker1, _, _ ->
            val selectHour = timePicker1.hour
            val selectMinute = timePicker1.minute
            val formattedTime = String.format("%02d:%02d", selectHour, selectMinute)
            timerChip.text = formattedTime

        }

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)


        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.contains(dateChip.id)){
                timerChip.isChecked = false
                timePicker.visibility = View.GONE
                datePicker.visibility = View.VISIBLE
                dateChip.isEnabled = false
                timerChip.isEnabled = true
            }else if (checkedIds.contains(timerChip.id)){
                dateChip.isChecked = false
                datePicker.visibility = View.GONE
                timePicker.visibility = View.VISIBLE
                timerChip.isEnabled = false
                dateChip.isEnabled = true

            }
        }

        val alarmNotification = view.findViewById<ImageView>(R.id.alarmOpen)

        alarmNotification.setOnClickListener {
            val header = arguments?.getString("headerValue")
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Schedule Alarm")
                .setMessage("Do you want to schedule alarm at ${dateChip.text } ${timerChip.text}")
                .setPositiveButton("okay") { dialogInterface, _ ->
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MINUTE, 1)

                  //  AlarmScheduler.scheduleAlarm(requireContext(),  "${dateChip.text} ${timerChip.text}",header!!)

                    val dataToSend = "${dateChip.text} ${timerChip.text}"
                    alarmViewModel.scheduleAlarm(dataToSend)
                    val listener = activity as? BottomSheetListener
                    listener?.onDataReceived(dataToSend)
                    dialogInterface.dismiss()
                    dismiss()
                }.show()


        }

    }
    private fun getTenMinutesLaterTimeInIST24Format(): String {
        // Get the current time in IST
        val currentTimeIST: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"))
        } else {
            Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"))
        }

        // Calculate 10 minutes later
        currentTimeIST.add(Calendar.MINUTE, 10)

        // Format the result
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(currentTimeIST.time)
    }

    companion object {
        const val TAG = "TAG"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodayDate(): String {
        val today = LocalDate.now()
        return today.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
    interface BottomSheetListener {
        fun onDataReceived(data: String)
    }

}