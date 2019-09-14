package com.example.mypharma.Views


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.example.mypharma.Models.User
import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.RoomDataBase.RoomService
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = activity!!.getSharedPreferences("fileName",Context.MODE_PRIVATE)
        val connected= pref.getBoolean("connected",false)
        if (connected==false){
            toast("Veuillez vous connectez")
            startActivity(intentFor<MainActivity>())}
        val nss= pref.getInt("nss",0)
        logout.setOnClickListener { view->
            pref.edit {
                putBoolean("connected",false)
                startActivity(intentFor<MainActivity>())
            }
        }
        val user=RoomService.appDataBase.getPharmaciesDao().getUserById(nss)
        if (user==null) {
            val call = RetrofitService.endpoint.getUserById(nss)
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>?, response:
                    Response<User>?
                ) {
                    val user = response?.body()

                    if (user != null) {
                        RoomService.appDataBase.getPharmaciesDao().addUser(user)
                        nom.text = user.nom
                        prenom.text = user.prenom
                        adresse.text = user.adresse
                        phone.text = user.telephone
                        user_nss.text = user.nss.toString()

                    } else {
                        toast("erreur")
                    }


                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Toast.makeText(ctx, t.toString(), Toast.LENGTH_SHORT).show()

                }
            })
        }
        else{
            nom.text = user.nom
            prenom.text = user.prenom
            adresse.text = user.adresse
            phone.text = user.telephone
            user_nss.text = user.nss.toString()
        }
    }
}
