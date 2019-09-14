package com.example.mypharma.Controllers

import android.app.Activity
import android.view.View
import com.example.mypharma.Adapters.PharamacieAdapter
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import kotlinx.android.synthetic.main.fragment_select_pharmacie.*

import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PharmaciesCommandesContoller {
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

}