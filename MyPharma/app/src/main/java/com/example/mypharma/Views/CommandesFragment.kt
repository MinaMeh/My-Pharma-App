package com.example.mypharma.Views


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.mypharma.Controllers.CommandesController
import com.example.mypharma.Models.Commande

import com.example.mypharma.R
import kotlinx.android.synthetic.main.fragment_commandes.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast


class CommandesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commandes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = activity!!.getSharedPreferences("fileName", Context.MODE_PRIVATE)

        val connected= pref.getBoolean("connected",false)
        if (connected==false){
            toast("Veuillez vous connectez")
            startActivity(intentFor<MainActivity>())}
        val commandeCtr= CommandesController()
        commandeCtr.loadData(activity!!)

        listCommandes.setOnItemClickListener { adapterView, view, i, l ->
            val commande = (adapterView.getItemAtPosition(i) as Commande)
            var bundle = bundleOf("commande_id" to commande.commande_id)
          /*  val fragment= CommandeDetailsFragment()
            fragment.arguments=bundle
            fragmentManager!!.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()*/
            view.findNavController().navigate(R.id.action_commandesFragment_to_commandeDetailsFragment,bundle)
        }
        add.setOnClickListener { view->
          /* val fragment= SelectVilleFragment()
            fragmentManager!!.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()*/

            view.findNavController().navigate(R.id.action_commandesFragment_to_selectPharmacieFragment)
        }
    }
}
