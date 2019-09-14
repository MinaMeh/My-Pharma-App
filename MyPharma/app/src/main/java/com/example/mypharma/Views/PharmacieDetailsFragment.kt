package com.example.mypharma.Views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypharma.Controllers.PharmaciesContoller

import com.example.mypharma.R

class PharmacieDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pharmacie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pharmacie_id = arguments?.getInt("pharmacie_id")
        val pharma= PharmaciesContoller()
        pharma.loadDetail(activity!!, pharmacie_id!!)

    }
}
