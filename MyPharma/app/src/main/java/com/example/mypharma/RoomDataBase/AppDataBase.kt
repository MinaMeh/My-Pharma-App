package com.example.mypharma.RoomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mypharma.Models.Commande
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Models.User
import com.example.mypharma.Models.Ville
import com.example.mypharma.RoomDAO.CommandesDao
import com.example.mypharma.RoomDAO.PharmaciesDao

@Database(entities = arrayOf(Ville::class, Pharmacie::class, Commande::class, User::class),version = 1)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getPharmaciesDao():PharmaciesDao
    abstract fun getCommandesDao():CommandesDao


}
