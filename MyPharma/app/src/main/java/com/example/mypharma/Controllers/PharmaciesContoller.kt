package com.example.mypharma.Controllers

import android.app.Activity
import android.content.ActivityNotFoundException
import android.view.View
import com.example.mypharma.Adapters.PharamacieAdapter
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import kotlinx.android.synthetic.main.fragment_pharmacie.*

import kotlinx.android.synthetic.main.fragment_pharmacie_details.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import org.jetbrains.anko.act


class PharmaciesContoller {
    fun loadData(act: Activity, ville_id: String) {
        act.progressBar2.visibility = View.VISIBLE
        // Get cities from SQLite DB

        var pharmacies = RoomService.appDataBase.getPharmaciesDao().getPharmaciesByVille(ville_id)

        if (pharmacies?.size == 0) {
            // If the list of cities is empty, load from server and save them in SQLite DB
             getPharmaciesFromRemote(act, ville_id)
        }
        else {
            act.progressBar2.visibility = View.GONE
            act.pharmaciesList.adapter = PharamacieAdapter(act!!, pharmacies!!)


        }

    }
    fun loadAllData(act: Activity) {
        act.progressBar2.visibility = View.VISIBLE
        // Get cities from SQLite DB

        var pharmacies = RoomService.appDataBase.getPharmaciesDao().getPharmacies()

        if (pharmacies?.size == 0) {
            // If the list of cities is empty, load from server and save them in SQLite DB
            getAllPharmaciesFromRemote(act)
        }
        else {
            act.progressBar2.visibility = View.GONE
            act.pharmaciesList.adapter = PharamacieAdapter(act!!, pharmacies!!)


        }

    }

    private fun getAllPharmaciesFromRemote(act: Activity) {
        val call = RetrofitService.endpoint.getPharmacies()
        call.enqueue(object : Callback<List<Pharmacie>> {
            override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {
                act.progressBar2.visibility = View.GONE
                if (response?.isSuccessful!!) {
                    val pharmacies = response?.body()!!
                    act.pharmaciesList.adapter = PharamacieAdapter(act, pharmacies)

                    act.progressBar2.visibility = View.GONE
                    // save cities in SQLite DB
                    RoomService.appDataBase.getPharmaciesDao().addPharmacies(pharmacies!!)

                } else {
                    act.toast(response.toString())
                }
            }

            override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                act.progressBar2.visibility = View.GONE
                act.toast(t.toString())
            }


        })

    }

    private fun getPharmaciesFromRemote(act: Activity, ville_id: String) {

        val call = RetrofitService.endpoint.getPharmaciesParVille(ville_id)
        call.enqueue(object : Callback<List<Pharmacie>> {
            override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {
                act.progressBar2.visibility = View.GONE
                if (response?.isSuccessful!!) {
                     val pharmacies = response?.body()!!
                    act.pharmaciesList.adapter = PharamacieAdapter(act, pharmacies)

                    act.progressBar2.visibility = View.GONE
                    // save cities in SQLite DB
                    RoomService.appDataBase.getPharmaciesDao().addPharmacies(pharmacies!!)

                } else {
                    act.toast(response.toString())
                }
            }

            override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                act.progressBar2.visibility = View.GONE
                act.toast(t.toString())
            }


        })
    }
    fun loadDetail(act:Activity,pharmacie_id:Int) {
        act.progressBar3.visibility = View.VISIBLE
        val pharmacie = RoomService.appDataBase.getPharmaciesDao().getPharmacieById(pharmacie_id)
        if(pharmacie==null) {
            // if the city details don't exist, load the details from server and update SQLite DB
            loadDetailFromRemote(act,pharmacie_id)
        }
        else {
            act.progressBar3.visibility = View.GONE
            displayDatail(act, pharmacie!!)
        }

    }

    private fun loadDetailFromRemote(act:Activity,pharmacie_id:Int) {
        val call = RetrofitService.endpoint.getPharmaciesById(pharmacie_id)
        call.enqueue(object : Callback<Pharmacie> {
            override fun onResponse(call: Call<Pharmacie>?, response: Response<Pharmacie>?) {
                act.progressBar3.visibility = View.GONE
                if(response?.isSuccessful!!) {
                    var pharmacie = response?.body()

                    displayDatail(act,pharmacie!!)
                    // update the city in the SQLite DB to support offline mode
                    RoomService.appDataBase.getPharmaciesDao().updatePharmacie(pharmacie)
                    // update ViewModel

                }
                else {
                    act.toast(response.toString())

                }


            }

            override fun onFailure(call: Call<Pharmacie>?, t: Throwable?) {
                act.progressBar3.visibility = View.GONE
                act.toast(t.toString())

            }
        })
    }

    fun displayDatail(act: Activity,pharmacie: Pharmacie) {

        act.adresse.text= pharmacie.adresse
        act.user_nss.text  = pharmacie.telephone
        act.fb.text = "page facebook"
       // act.horaire.text= "Ouverte de "+pharmacie.ouverture+ "jusqu'à "+pharmacie.fermeture
        act.ouver.text=pharmacie.ouverture
        act.femet.text=pharmacie.fermeture
        act.caisses.text = "Caisses conventionnées: "+ pharmacie.caisses
        act.fb.setOnClickListener { view->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pharmacie.fb))
                act.startActivity(intent)

            }
            catch (e: ActivityNotFoundException){

            }
        }
        act.voir_position.setOnClickListener{view->
            val gmmIntentUri = Uri.parse("geo:"+pharmacie.latitude+","+pharmacie.longitude+"?q="+pharmacie.latitude+","+pharmacie.longitude+"(pharmacie)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            act.startActivity(mapIntent)

        }

    }


}