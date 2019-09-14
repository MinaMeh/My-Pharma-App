package com.example.mypharma.RoomDataBase

import android.content.Context
import androidx.room.Room

object RoomService {
    lateinit var context: Context

    val appDataBase: AppDataBase by lazy {
        Room.databaseBuilder(context,AppDataBase::class.java,"pharmadb").allowMainThreadQueries().build()
    }
}