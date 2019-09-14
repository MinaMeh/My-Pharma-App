package com.example.mypharma.Retrofit

import android.database.Observable
import com.example.mypharma.Models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Endpoint {

    @POST("/sendsms")
    fun sendsms(@Body user: User): Call<String>

    @POST("/adduser")
    fun addUser(@Body user:User): Call<String>

    @POST("/updatePassword")
    fun updateUser (@Body newPass: NewPass): Call<String>

    @GET("/getUser/{telephone}")
    fun getUserByPhone(@Path("telephone") telephone: String): Call<List<User>>

    @GET("/getVilles")
    fun getVilles(): Call<List<Ville>>

    @GET("/getPharmacies")
    fun getPharmacies(): Call<List<Pharmacie>>

    @GET("getPharmaciesParVille/{ville}")
    fun getPharmaciesParVille(@Path("ville") ville: String): Call<List<Pharmacie>>

    @GET("getPharmacie/{id}")
    fun getPharmaciesById(@Path("id") id: Int): Call<Pharmacie>

    @GET("getUserCommandes/{user}")
    fun getUserCommandes(@Path("user") user: Int): Call<List<Commande>>

    @GET ("getCommandePharmacie/{commande}")
    fun getCommandePharmacieN(@Path ("commande") commande: Int): Call<Pharmacie>

    @GET ("getCommandeById/{commande}")
    fun getCommandeById(@Path ("commande") commande: Int): Call<Commande>

    @POST("/addCommande/")
    fun addCommande(@Body commande: Commande): Call<String>

    @GET ("/getNbCommandes")
    fun getNbCommandes(): Call<Int>

    @Multipart
    @POST ("/upload")
    fun uploadPhoto(@Part photo: MultipartBody.Part ): Call <String>

    @GET("getUserById/{nss}")
    fun getUserById(@Path("nss") nss: Int): Call<User>


    @POST("/updateToken")
    fun updateToken (@Body user: User): Call<String>

    @POST("/updateCommande")
    fun updateCommande (@Body commande: Commande): Call<String>


}