package com.example.mypharma.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "pharmacies")
data class Pharmacie (
    @PrimaryKey
    var pharmacie_id: Int,
    var adresse: String,
    var ouverture: String,
    var fermeture: String,
    var telephone: String,
    var caisses: String,
    var fb: String,
    var longitude: Double,
    var latitude: Double,
    var ville_id: Int
): Serializable