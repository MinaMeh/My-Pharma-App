package com.example.mypharma.ViewModels

import android.app.Activity
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mypharma.Adapters.PharamacieAdapter
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import kotlinx.android.synthetic.main.fragment_pharmacie.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PharmacieModel: ViewModel() {


    var pharmacie: Pharmacie? = null
    var pharmacies:List<Pharmacie>? = null


    fun loadData(act: Activity, ville_id: String) {
        act.progressBar2.visibility = View.VISIBLE
        // Get cities from SQLite DB
        pharmacies = RoomService.appDataBase.getPharmaciesDao().getPharmaciesByVille(ville_id)

        if (pharmacies?.size == 0) {
            // If the list of cities is empty, load from server and save them in SQLite DB
            getPharmaciesFromRemote(act, ville_id)
        }
        else {
            act.progressBar2.visibility = View.GONE
            act.pharmaciesList.adapter = PharamacieAdapter(act, pharmacies!!)
        }




    }

    private fun getPharmaciesFromRemote(act: Activity, ville_id: String) {
        val call = RetrofitService.endpoint.getPharmaciesParVille(ville_id)
        call.enqueue(object : Callback<List<Pharmacie>> {
            override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {
                act.progressBar2.visibility = View.GONE
                if (response?.isSuccessful!!) {
                    pharmacies = response?.body()
                    act.toast("pharmacies= "+pharmacies.toString())
                    act.progressBar2.visibility = View.GONE
                    act.pharmaciesList.adapter = PharamacieAdapter(act, pharmacies!!)
                    // save cities in SQLite DB
                    RoomService.appDataBase.getPharmaciesDao().addPharmacies(pharmacies!!)
                } else {
                    act.toast("Une erreur s'est produite")
                }
            }

            override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                act.progressBar2.visibility = View.GONE
                act.toast("Une erreur s'est produite")
            }


        })
    }
/*
    fun loadDetail(act:Activity,idCity:Int) {
        act.progressBar2.visibility = View.VISIBLE
        // load city detail from SQLite DB
        this.city = RoomService.appDataBase.getCityDao().getCityById(idCity)
        if(this.city?.detailImage==null) {
            // if the city details don't exist, load the details from server and update SQLite DB
            loadDetailFromRemote(act,idCity)
        }
        else {
            act.progressBar2.visibility = View.GONE
            displayDatail(act, this.city!!)
        }

    }

    private fun loadDetailFromRemote(act:Activity,idCity:Int) {
        val call = RetrofitService.endpoint.getDetailCity(idCity)
        call.enqueue(object : Callback<City> {
            override fun onResponse(call: Call<City>?, response: Response<City>?) {
                act.progressBar2.visibility = View.GONE
                if(response?.isSuccessful!!) {
                    var cityDetail = response?.body()
                    cityDetail = city!!.copy(
                        description =cityDetail?.description,
                        detailImage = cityDetail?.detailImage,
                        places = cityDetail?.places)
                    displayDatail(act,cityDetail)
                    // update the city in the SQLite DB to support offline mode
                    RoomService.appDataBase.getCityDao().updateCity(cityDetail)
                    // update ViewModel
                    this@CityModel.city = cityDetail

                }
                else {
                    act.toast("Une erreur s'est produite")

                }


            }

            override fun onFailure(call: Call<City>?, t: Throwable?) {
                act.progressBar2.visibility = View.GONE
                act.toast("Une erreur s'est produite")

            }
        })
    }

    fun displayDatail(act: Activity,city: City) {
        Glide.with(act).load(baseUrl + city.detailImage).apply(
            RequestOptions().placeholder(R.drawable.place_holder)
        ).into(act.imageDetail)

        act.nameDetail.text = city.name
        act.numbertouristDetail.text  = city.touristNumber
        act.description.text = city.description
        act.places.text = act.getString(R.string.places)+city.places
    }

*/
}