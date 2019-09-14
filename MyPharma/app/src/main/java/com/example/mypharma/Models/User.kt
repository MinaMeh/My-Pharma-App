package com.example.mypharma.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity (tableName = "users")
data class User (
    @PrimaryKey
    var nss: Int,
    var nom:String,
    var prenom: String,
    var adresse: String,
    var telephone: String,
    var password: String,
    var device_token: String
): Serializable