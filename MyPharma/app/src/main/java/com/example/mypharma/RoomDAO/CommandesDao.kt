package com.example.mypharma.RoomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mypharma.Models.Commande
import com.example.mypharma.Models.Pharmacie

@Dao
interface CommandesDao {
    @Query("select * from commandes")
    fun getCommandes():List<Commande>

    @Query("select * from commandes where commande_id=:id_cmd")
    fun getCommandeById(id_cmd: Int):Commande

    @Insert
    fun addCommandes(commandes:List<Commande>)

    @Update
    fun updateCommande(commande: Commande)
}