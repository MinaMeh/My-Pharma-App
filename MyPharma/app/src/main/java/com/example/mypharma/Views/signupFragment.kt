package com.example.mypharma.Views

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.example.mypharma.Controllers.AuthentificationController
import com.example.mypharma.Models.User
import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import kotlinx.android.synthetic.main.fragment_signup.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class signupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phone.setOnClickListener { view->
        }
        signup.setOnClickListener { view->
            val telephone = "+213"+phone.text.toString()
            val nss= user_nss.text.toString().toInt()
            val nom= nom.text.toString()
            val prenom= prenom.text.toString()
            val adresse= adress.text.toString()
            val password= generatePassword()
            val pref = activity!!.getSharedPreferences("fileName", Context.MODE_PRIVATE)
            val token = pref.getString("token","token")
            var user= User(nss,nom, prenom,adresse,telephone,password,token)
            var auth =AuthentificationController()
            val call= RetrofitService.endpoint.getUserByPhone(telephone)
            call.enqueue(object: Callback<List<User>> {
                override fun onResponse(call: Call<List <User>>?, response:
                Response<List<User>>?) {
                    val Listuser= response?.body()
                    if (Listuser?.isNotEmpty()!!){
                        toast("Ce numéro de téléphone est déja utilisé, essayez de se connecter")
                    }
                    else{
                        auth.addUser(user, activity!!)
                        auth.sendSms(user,activity!!,view)
                    }


                }
                override fun onFailure(call: Call<List <User>>?, t: Throwable?) {
                    Toast.makeText(activity!!,t.toString(),Toast.LENGTH_SHORT).show()

                }
            })




            }
    }





    private fun generatePassword(): String {
        var passwd= (Math.random()*100000 ).toInt()
        return passwd.toString()
    }
}
