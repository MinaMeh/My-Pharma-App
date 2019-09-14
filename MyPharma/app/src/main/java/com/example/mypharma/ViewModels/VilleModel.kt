package com.example.mypharma.ViewModels

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mypharma.Adapters.VilleAdapter
import com.example.mypharma.Models.Ville
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import kotlinx.android.synthetic.main.fragment_pharmacie.*
import kotlinx.android.synthetic.main.fragment_villes.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VilleModel: ViewModel() {


    var ville: Ville? = null
    var villes:List<Ville>? = null


    fun loadData(act: Activity) {
        // Get cities from SQLite DB
        villes = RoomService.appDataBase.getPharmaciesDao().getVilles()

        if (villes?.size == 0) {
            // If the list of cities is empty, load from server and save them in SQLite DB
            getCitiesFromRemote(act)
        }
        else {
            act.spinner.adapter = VilleAdapter(act, villes!!)
        }




    }

    private fun getCitiesFromRemote(act:Activity) {
        val call = RetrofitService.endpoint.getVilles()
        call.enqueue(object : Callback<List<Ville>> {
            override fun onResponse(call: Call<List<Ville>>?, response: Response<List<Ville>>?) {
//                act.progressBar.visibility = View.GONE
                if (response?.isSuccessful!!) {
                    villes = response?.body()
                   // act.spinner.adapter = VilleAdapter(act, villes!!)
                    // save cities in SQLite DB
                    RoomService.appDataBase.getPharmaciesDao().addVille(villes!!)
                } else {
                    act.toast("Une erreur s'est produite")
                }
            }

            override fun onFailure(call: Call<List<Ville>>?, t: Throwable?) {
                act.toast(t.toString())
            }


        })
    }
}