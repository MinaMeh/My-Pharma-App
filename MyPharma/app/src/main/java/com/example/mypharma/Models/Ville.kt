package com.example.mypharma.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "villes")
data  class Ville (
    @PrimaryKey
    var ville_id: Int,
    var nom: String
): Serializable