package com.siddiqui.alarmapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.siddiqui.alarmapp.adapter.AlarmAdapter
import com.siddiqui.alarmapp.alarmBroadcast.AlarmReceiver
import com.siddiqui.alarmapp.databinding.ActivityMainBinding
import com.siddiqui.alarmapp.permission.NotificationPermissionHandler
import com.siddiqui.alarmapp.repo.MediaPlayerManager
import com.siddiqui.alarmapp.viewmodel.AlarmViewModel
import com.siddiqui.alarmapp.viewmodel.AlarmViewModelFactory
import com.siddiqui.alarmapp.viewmodel.RecyclerViewModel

class MainActivity : AppCompatActivity(), AlarmBottomSheet.BottomSheetListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmViewModel: AlarmViewModel
    private val dateList:MutableList<String> = mutableListOf()
    private lateinit var recyclerViewModel: RecyclerViewModel
    private var viewManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewModel = ViewModelProvider(this)[RecyclerViewModel::class.java]

        // Initialize the repository and ViewModel Factory
        // Provide any necessary dependencies to the repository

        val factory = AlarmViewModelFactory(application)
        alarmViewModel = ViewModelProvider(this, factory)[AlarmViewModel::class.java]

        binding.addAlarmBtn.setOnClickListener {
            if (!isNotificationPermissionGranted()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
                    }
                }
            }else {

                val alarmBottomSheet = AlarmBottomSheet()
                alarmBottomSheet.show(supportFragmentManager, AlarmBottomSheet.TAG)
               /* val scheduledTimeMillis = 1696232820000
                alarmViewModel.scheduleAlarm(scheduledTimeMillis)
                Toast.makeText(this, "Notification is enable", Toast.LENGTH_SHORT).show()
                val currentTimeMillis = System.currentTimeMillis()
                if (currentTimeMillis > scheduledTimeMillis){
                    val mediaPlayerManager = MediaPlayerManager.getInstance(this)
                    mediaPlayerManager.stop()
                }*/
            }
        }
        binding.emptyLinearLayout.hide()
        initialiseAdapter()
    }

    override fun onResume() {
        super.onResume()
        if (dateList.isEmpty()){
            binding.emptyLinearLayout.show()
        }
    }
    private fun initialiseAdapter(){
        binding.recyclerView.layoutManager = viewManager
        observeData()
    }
    private fun observeData(){
        recyclerViewModel.lst.observe(this, Observer{
            binding.recyclerView.adapter = AlarmAdapter(recyclerViewModel, it, this)
        })
    }

    private fun isNotificationPermissionGranted():Boolean{
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            // Permission was granted, you can proceed with the logic that requires this permission
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()

            } else {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Permission Required")
                    .setMessage("This permission is required for receiving reminder notifications. Please grant the permission in the app's settings.")
                    .setPositiveButton("Open Settings") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", this.packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .create()

                dialog.show()
                // Permission denied, handle this case (e.g., show a message to the user)
            }

        }
    }

    override fun onDataReceived(data: String) {

    }
    private fun View.show(){
       visibility = View.VISIBLE
    }
    private fun  View.hide(){
        visibility = View.GONE
    }

}