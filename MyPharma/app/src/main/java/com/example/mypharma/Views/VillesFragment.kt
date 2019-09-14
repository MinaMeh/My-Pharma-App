package com.example.mypharma.Views


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_villes.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast


class VillesFragment : Fragment() {
    var textlength=0
    var arrayList= ArrayList<Ville>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_villes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val view1= view

        val cityModel = ViewModelProviders.of(activity!!).get(VilleModel::class.java)
        // If the list of cities is null, load the list from DB
        if (cityModel.villes==null) {
            cityModel.loadData(activity!!)
        }
        else {
            // After the rotation of the screen, use cities of the ViewModel instance
            listVilles.adapter = VilleAdapter(activity!!, cityModel.villes!!)
        }


        listVilles.setOnItemClickListener { adapterView, view, i, l ->
            val ville = (adapterView.getItemAtPosition(i) as Ville)
            var bundle = bundleOf("ville_id" to ville.ville_id)
          //  view.findNavController().navigate(R.id.action_villesFragment_to_pharmacieFragment ,bundle)

        }
        search.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textlength = search.text.length
                arrayList.clear()
                for (ville in cityModel.villes!!) {
                    if (textlength <= ville.nom.length) {
                        if (ville.nom.toLowerCase().trim().contains(
                                search.text.toString().toLowerCase().trim())) {
                            arrayList.add(ville)
                        }
                    }
                }
                listVilles.adapter = VilleAdapter(activity!!, arrayList)


            }

        })
        nearest.setOnClickListener { view->
//view.findNavController().navigate(R.id.action_villesFragment_to_nearestPharmaciesFragment)
            startActivity(intentFor<BasicMapDemoActivity>())
        }

    }
}

