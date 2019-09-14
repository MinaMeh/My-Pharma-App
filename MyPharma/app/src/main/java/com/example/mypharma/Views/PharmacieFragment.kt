package com.example.mypharma.Views


import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.mypharma.Adapters.PharamacieAdapter
import com.example.mypharma.Controllers.PharmaciesContoller
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.Models.Ville
import com.example.mypharma.R
import com.example.mypharma.ViewModels.PharmacieModel
import com.example.mypharma.ViewModels.VilleModel
import kotlinx.android.synthetic.main.fragment_pharmacie.*
import kotlinx.android.synthetic.main.ville_layout.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


class PharmacieFragment : Fragment() {



  /*  private fun getVillesNames():ArrayList<String> {
        val villes_names= arrayListOf<String>()

        for (ville in villes!!){
            villes_names.add(ville.nom)
        }
        return  villes_names

    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharmacie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       // val ville_id= arguments?.getInt("ville_id",1)
        val pharma= PharmaciesContoller()
        val cityModel = ViewModelProviders.of(activity!!).get(VilleModel::class.java)
        var villes=cityModel.villes
        val villes_names= ArrayList<String>()
        if (cityModel.villes==null) {
            cityModel.loadData(activity!!)
        }
        villes=cityModel.villes

        villes_names.add("Toutes les pharmacies")
        villes_names.add("Les pharamacies les plus proches")
        for (ville in villes!!){
            villes_names.add(ville.nom)
        }


        var aa = ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, villes_names)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
       spinner.adapter=aa
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = villes_names.get(position)
                if (type=="Toutes les pharmacies"){
                    pharma.loadAllData(activity!!)

                }

                else{
                    if (type=="Les pharamacies les plus proches")
                        startActivity(intentFor<BasicMapDemoActivity>())

                    else
                        pharma.loadData(activity!!,type)
                }
            }

        }


       // pharma.loadData(activity!!,ville_id!!)

        pharmaciesList.setOnItemClickListener { adapterView, view, i, l ->
            val pharmacie = (adapterView.getItemAtPosition(i) as Pharmacie)
            var bundle = bundleOf("pharmacie_id" to pharmacie.pharmacie_id)
            view.findNavController().navigate(R.id.action_pharmacieFragment_to_pharmacieDetailsFragment,bundle)
        }

    }




}

