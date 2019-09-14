/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mypharma.Views


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mypharma.Controllers.PharmaciesContoller
import com.example.mypharma.Models.Pharmacie
import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.jetbrains.anko.toast
import com.google.android.gms.tasks.Task
import org.jetbrains.anko.act
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val PERMISSION_REQUEST = 10

class BasicMapDemoActivity :
        AppCompatActivity(),
        OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?)=false

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    val SYDNEY = LatLng(36.720375, 3.180191)
    val ZOOM_LEVEL = 13f
    var options=MarkerOptions()
    private lateinit var lastLocation: Location


    private val latlngs = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_map_demo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                getLocation()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            getLocation()
        }

        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(com.example.mypharma.R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just move the camera to Sydney and add a marker in Sydney.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        mMap=googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)

      /*  latlngs.add(LatLng(36.72,3.18))
        latlngs.add(LatLng(36.77885, 3.175102))
        latlngs.add(
            LatLng(36.705314, 3.173841)
        )*/
/*
        with(mMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
            for (point in latlngs) {
                options.position(point)
                options.title("Ma position")
                options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.profile))
                addMarker(options)
            }

        }*/
     //   setUpMap()


    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }
    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                getLocation()

        }
    }

    private fun showLocation() {

    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                 val locationListener: LocationListener = object : LocationListener {
                     override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                     }

                     override fun onProviderEnabled(provider: String?) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                     }

                     override fun onProviderDisabled(provider: String?) {
                         TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                     }

                     override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationGps = location

                            with(mMap) {
                                moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), ZOOM_LEVEL))
                                val point = LatLng(location.latitude, location.longitude)
                                options.position(point)
                                options.title("Ma position")
                                addMarker(options)


                            }
                        }
                    }

                }


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER!!, 60000, 0F, locationListener )

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0F, object : LocationListener {
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderEnabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onProviderDisabled(provider: String?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            with(mMap) {
                                moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), ZOOM_LEVEL))

                                val point = LatLng(location.latitude, location.longitude)
                                options.position(point)
                                options.title("Ma position")
                                addMarker(options)
                               /* for (i in listOf(0,1)){
                                    val point = LatLng(pharmacies.get(i).latitude,pharmacies.get(i).longitude)
                                    options.position(point)
                                    options.title(i.toString())
                                    options.snippet(pharmacies.get(i).adresse)
                                    addMarker(options)
                                }*/
                                val pharmacies=getNearestPharmacies(location)


                            }
                    }}


                 })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){

                }else{

                }
            }

        } else {
           // startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    fun getNearestPharmacies (location: Location): List<Pharmacie>{
        var listPharmacies= ArrayList<Pharmacie>()
        var distances: MutableMap<Int, Float> = mutableMapOf<Int, Float>()

        var pharmacies=ArrayList<Pharmacie>()
        pharmacies= RoomService.appDataBase.getPharmaciesDao().getPharmacies() as ArrayList<Pharmacie>
        if (pharmacies.size==0) {
            val call = RetrofitService.endpoint.getPharmacies()

            call.enqueue(object : Callback<List<Pharmacie>> {
                override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {
                    if (response?.isSuccessful!!) {
                        pharmacies = ArrayList(response?.body()!!)
                    } else {
                        Log.d("MAP", "Une erreur s'est produite")
                    }
                }

                override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                    Log.d("MAP", t.toString())

                }


            })
        }
        for (pharmacie in pharmacies!!){
            val pharmacieLocation= Location("pharmacie")
            pharmacieLocation.latitude=pharmacie.latitude
            pharmacieLocation.longitude=pharmacie.longitude
            val distance= location.distanceTo(pharmacieLocation)
            distances!!.put(pharmacie.pharmacie_id,distance)

        }

        var sorted = distances!!.toList().sortedBy { (_, value) -> value}.toMap()
        var i=1

        Log.d("MAP","sorted  = "+sorted.toString())
        var listIndice: MutableList<Int> = mutableListOf<Int>()
        for (element in sorted){
            Log.d("MAP","i  = "+i.toString())

            val pharma= RoomService.appDataBase.getPharmaciesDao().getPharmacieById(element.key)
            if (pharma==null){
            val call = RetrofitService.endpoint.getPharmaciesById(element.key)
            call.enqueue(object : Callback<Pharmacie> {
                override fun onResponse(call: Call<Pharmacie>?, response: Response<Pharmacie>?) {

                        if (response?.isSuccessful!!) {
                            val pharama = response.body()!!

                            listPharmacies.add(pharama)
                            Log.d("MAP", "id  = " + pharama.pharmacie_id.toString())
                            val point = LatLng(pharama.latitude, pharama.longitude)
                            options.position(point)
                            options.title("pharmacie")
                            options.snippet(pharama.adresse)
                            mMap.addMarker(options)

                        } else {

                            Log.d("MAP", response.toString())

                        }

                }
                override fun onFailure(call: Call<Pharmacie>?, t: Throwable?) {
                    Log.d("MAP",t.toString())

                }


            })}
            else{
                val point = LatLng(pharma.latitude, pharma.longitude)
                options.position(point)
                options.title(i.toString())
                options.snippet(pharma.adresse)
                mMap.addMarker(options)
            }
            i=i+1


        }
        Log.d("MAP","list  = "+listPharmacies.toString())


        return listPharmacies
    }

}



