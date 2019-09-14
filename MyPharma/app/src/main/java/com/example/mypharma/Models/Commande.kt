package com.example.mypharma.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "commandes")
data class Commande (
    @PrimaryKey
    var commande_id: Int,
    var photo: String,
    var user_id: Int,
    var pharmacie_id: Int,
    var etat: String,
    var date: String,
    var amount:Int
): Serializable