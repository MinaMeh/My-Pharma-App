package com.example.mypharma

import android.app.Application
import com.example.mypharma.RoomDataBase.RoomService

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        RoomService.context = applicationContext
    }
}