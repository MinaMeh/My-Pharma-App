package com.example.mypharma.Views


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mypharma.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CameraPosition
import kotlinx.android.synthetic.main.fragment_nearest_pharmacies.*
import kotlinx.android.synthetic.main.fragment_pharmacie_details.*
import org.jetbrains.anko.support.v4.toast


class NearestPharmaciesFragment : Fragment(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private  val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onMapReady(googleMap: GoogleMap?) {
        //googleMap?: return
        mMap=googleMap!!
        toast(LatLng(-10.0,-20.0).toString())

        with(mMap!!){
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-10.0,-20.0),13f))
            addMarker(MarkerOptions().position(LatLng(-10.0,-20.0)))
            toast(LatLng(-10.0,-20.0).toString())
        }
        setUpMap()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearest_pharmacies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = activity!!.supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)



    }
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            mMap!!.isMyLocationEnabled = true

            }


            return
        }
    }



