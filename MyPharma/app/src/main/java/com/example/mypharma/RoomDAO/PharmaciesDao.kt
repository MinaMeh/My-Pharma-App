package com.example.mypharma.RoomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Models.User
import com.example.mypharma.Models.Ville

@Dao

interface PharmaciesDao {
    @Query("select * from villes")
    fun getVilles():List<Ville>

    @Query("select * from villes where ville_id=:idVille")
    fun getCityById(idVille:Int):Ville

    @Query("select * from pharmacies")
    fun getPharmacies():List<Pharmacie>

    @Query("select * from pharmacies where pharmacie_id=:id_pharmacie")
    fun getPharmacieById(id_pharmacie:Int):Pharmacie

    @Query("select * from pharmacies join villes on pharmacies.ville_id=villes.ville_id  where villes.nom=:ville")
    fun getPharmaciesByVille(ville:String):List<Pharmacie>

    @Query("select * from users where nss=:idUser")
    fun getUserById(idUser:Int):User

    @Insert
    fun addVille(cities:List<Ville>)

    @Insert
    fun addUser(user:User)

    @Insert
    fun addPharmacies(pharmacies:List<Pharmacie>)
    @Update
    fun updatePharmacie(pharmacie:Pharmacie)

}