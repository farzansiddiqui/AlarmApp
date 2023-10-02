package com.siddiqui.alarmapp.repo

import android.content.Context
import android.media.MediaPlayer
import com.siddiqui.alarmapp.R

class MediaPlayerManager private constructor(context: Context){

    private val mediaPlayer:MediaPlayer = MediaPlayer.create(context, R.raw.bell_ring)

    companion object{
        @Volatile
        private var instance:MediaPlayerManager? = null

        fun getInstance(context: Context):MediaPlayerManager{
            return instance ?: synchronized(this){
                instance ?: MediaPlayerManager(context).also {
                    instance = it }
            }
        }
    }
    fun start() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.release()

    }
}