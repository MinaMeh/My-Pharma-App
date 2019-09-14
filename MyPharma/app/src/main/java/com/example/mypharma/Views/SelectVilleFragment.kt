package com.example.mypharma.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.mypharma.Adapters.VilleAdapter
import com.example.mypharma.Models.Ville

import com.example.mypharma.R
import com.example.mypharma.ViewModels.VilleModel
import kotlinx.android.synthetic.main.fragment_select_ville.*


class SelectVilleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_ville, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cityModel = ViewModelProviders.of(activity!!).get(VilleModel::class.java)
        // If the list of cities is null, load the list from DB
        if (cityModel.villes==null) {
            cityModel.loadData(activity!!)
            progressBar.visibility=View.GONE
        }
        else {
            // After the rotation of the screen, use cities of the ViewModel instance
            listVilles.adapter = VilleAdapter(activity!!, cityModel.villes!!)
        }


        listVilles.setOnItemClickListener { adapterView, view, i, l ->
            val ville = (adapterView.getItemAtPosition(i) as Ville)
            var bundle = bundleOf("ville_id" to ville.ville_id)
            //view.findNavController().navigate(R.id.action_selectVilleFragment_to_selectPharmacieFragment,bundle)
        }
    }
}
