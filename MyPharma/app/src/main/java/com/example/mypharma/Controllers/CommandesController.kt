package com.example.mypharma.Controllers

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mypharma.Adapters.CommandesAdapter
import com.example.mypharma.Models.Commande
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.Views.CommandeDetailsFragment
import com.example.mypharma.baseUrl
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_commande.*
import kotlinx.android.synthetic.main.fragment_commande_details.*
import kotlinx.android.synthetic.main.fragment_commande_details.date
import kotlinx.android.synthetic.main.fragment_commande_details.pharmacie
import kotlinx.android.synthetic.main.fragment_commande_details.photo
import kotlinx.android.synthetic.main.fragment_commandes.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandesController {
    fun getCommandePharmacie (viewHolder: CommandesAdapter.ViewHolder, commande_id: Int): Pharmacie?{
        var pharmacie: Pharmacie?
        pharmacie=null
        val call = RetrofitService.endpoint.getCommandePharmacieN(commande_id)
        call.enqueue(object : Callback<Pharmacie> {
            override fun onResponse(call: Call<Pharmacie>?, response: Response<Pharmacie>?) {
                if(response?.isSuccessful!!) {
                     pharmacie = response?.body()!!
                    viewHolder.pharmacie.text=pharmacie!!.adresse

                }
                else {

                    pharmacie = null
                }

            }

            override fun onFailure(call: Call<Pharmacie>?, t: Throwable?) {
                pharmacie=null


            }
        })
        return pharmacie

    }
    fun getCommandePharmacie2 (act: Activity, commande_id: Int): Pharmacie? {
        var pharmacie: Pharmacie?
        pharmacie = null
        val call = RetrofitService.endpoint.getCommandePharmacieN(commande_id)
        call.enqueue(object : Callback<Pharmacie> {
            override fun onResponse(call: Call<Pharmacie>?, response: Response<Pharmacie>?) {
                if (response?.isSuccessful!!) {
                    pharmacie = response?.body()!!
                    act.pharmacie.text = pharmacie!!.adresse

                } else {

                    pharmacie = null
                }

            }

            override fun onFailure(call: Call<Pharmacie>?, t: Throwable?) {
                pharmacie = null


            }
        })
        return pharmacie
    }

        fun loadData(act: Activity) {
        act.progressBar4.visibility = View.VISIBLE


            getCommandesFromRemote(act)


    }

    private fun getCommandesFromRemote(act: Activity) {

        val pref = act.getSharedPreferences("fileName",Context.MODE_PRIVATE)
        val nss= pref.getInt("nss",1)
        val call = RetrofitService.endpoint.getUserCommandes(nss)
        call.enqueue(object : Callback<List<Commande>>{
            override fun onResponse(call: Call<List<Commande>>?, response: Response<List<Commande>>?) {
                act.progressBar4.visibility = View.GONE
                if (response?.isSuccessful!!) {
                    val commandes = response?.body()!!
                    act.listCommandes.adapter = CommandesAdapter(act, commandes)

                    act.progressBar4.visibility = View.GONE
                    // save cities in SQLite DB

                } else {
                    act.toast("Une erreur s'est produite")
                }
            }

            override fun onFailure(call: Call<List<Commande>>?, t: Throwable?) {
                act.progressBar4.visibility = View.GONE
                act.toast(t.toString())
            }


        })
    }
    fun loadDetail(act:Activity,id_cmd:Int) {
        act.progressBar5.visibility = View.VISIBLE
            loadDetailFromRemote(act,id_cmd)


    }

    private fun loadDetailFromRemote(act:Activity,id_cmd:Int) {
        val call = RetrofitService.endpoint.getCommandeById(id_cmd)
        call.enqueue(object : Callback<Commande> {
            override fun onResponse(call: Call<Commande>?, response: Response<Commande>?) {
                act.progressBar5.visibility = View.GONE
                if(response?.isSuccessful!!) {
                    var cmd = response?.body()


                    displayDatail(act,cmd!!)
                }
                else {
                    act.toast(response.toString())

                }


            }

            override fun onFailure(call: Call<Commande>?, t: Throwable?) {
                act.progressBar5.visibility = View.GONE
                act.toast(t.toString())

            }
        })
    }

    fun displayDatail(act: Activity,cmd: Commande) {
        Glide.with(act).load(baseUrl + cmd.photo).apply(
            RequestOptions().placeholder(R.drawable.logo)
        ).into(act.photo)
        getCommandePharmacie2(act,cmd.commande_id)
        act.date.text = cmd.date
        if (cmd.etat=="T"){
            act.amount.visibility=View.VISIBLE
        }
        if (cmd.etat=="T") {
            act.etat.text = "Traitée"
            act.amnt.visibility=View.VISIBLE
            act.amount.visibility=View.VISIBLE
            act.payer.visibility=View.VISIBLE
            act.amount.text= cmd.amount.toString()
        }

        else act.etat.text = cmd.etat
    }

    fun addCommande(view: View,ctx: Activity, cmd: Commande){
        val callAdd= RetrofitService.endpoint.addCommande(cmd)
        callAdd.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {
                ctx.progress.visibility=View.GONE

                Toast.makeText(ctx, "commande envoyée", Toast.LENGTH_SHORT)
                    .show()
                view!!.findNavController().navigate(R.id.action_addCommandeFragment_to_commandesFragment)


            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Toast.makeText(ctx,t.toString(), Toast.LENGTH_SHORT).show()

            }
        })
    }

}