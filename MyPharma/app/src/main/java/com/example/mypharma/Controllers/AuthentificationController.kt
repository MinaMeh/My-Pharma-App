package com.example.mypharma.Controllers

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.example.mypharma.Models.NewPass
import com.example.mypharma.Models.User
import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.Views.Main2Activity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.toast

class AuthentificationController {


        fun addUser(user: User, ctx: FragmentActivity?){
        val callAdd= RetrofitService.endpoint.addUser(user)
        callAdd.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("test","add"+t.toString())


            }
        })
    }

      fun sendSms(user: User, ctx: FragmentActivity?, view: View){
          val call= RetrofitService.endpoint.sendsms(user)
          call.enqueue(object: Callback<String> {
              override fun onResponse(call: Call<String>?, response:
              Response<String>?) {
                  var bundle= bundleOf("password" to user.password, "nss" to user.nss)
                  view.findNavController().navigate(R.id.action_signupFragment_to_confirmFragment,bundle)

              }
              override fun onFailure(call: Call<String>?, t: Throwable?) {
                  Log.d("test","sms"+t.toString())


              }
          })

      }

      fun updatePassword (newPass: NewPass, ctx: FragmentActivity?, view: View){
          val call= RetrofitService.endpoint.updateUser(newPass)
          call.enqueue(object: Callback<String> {
              override fun onResponse(call: Call<String>?, response:
              Response<String>?) {
                  val pref = ctx?.getSharedPreferences("fileName",Context.MODE_PRIVATE)
                  pref?.edit {
                      putBoolean("connected", true)
                      putInt("nss", newPass.nss)
                  }
                  ctx!!.startActivity(ctx!!.intentFor<Main2Activity>())
                  ctx.finish()

              }
              override fun onFailure(call: Call<String>?, t: Throwable?) {

              }
          })
      }
    fun updateToken (token: String, nss: Int){
      //  val pref = ctx?.getSharedPreferences("fileName",Context.MODE_PRIVATE)
       // val nss= pref!!.getInt("nss",0)
        Log.d("test", "nss "+nss)

        val callUser= RetrofitService.endpoint.getUserById(nss)
        callUser.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>?, response:
            Response<User>?) {
                val user= response!!.body()
                user!!.device_token=token
                Log.d("test", "user "+user.nss)

                val call= RetrofitService.endpoint.updateToken(user)
                call.enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>?, response:
                    Response<String>?) {
                        Log.d("test", "user token "+user.device_token)
                     //   Toast.makeText(ctx,response.toString(),Toast.LENGTH_SHORT).show()

                    }
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        //Toast.makeText(ctx,"failure",Toast.LENGTH_SHORT).show()

                    }
                })


            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                //Toast.makeText(ctx,"failure",Toast.LENGTH_SHORT).show()

            }
        })

    }
      fun connectUser(telephone: String, password: String,ctx: FragmentActivity?, view: View){

          val call= RetrofitService.endpoint.getUserByPhone(telephone)
          call.enqueue(object: Callback<List<User>> {
              override fun onResponse(call: Call<List <User>>?, response:
              Response<List<User>>?) {
                  val Listuser= response?.body()
                  if (Listuser?.isNotEmpty()!!){
                      val user= Listuser.first()
                      if (user.password==password){
                          val pref = ctx?.getSharedPreferences("fileName",Context.MODE_PRIVATE)
                          pref?.edit {
                              putBoolean("connected", true)
                              putInt("nss", user.nss)

                          }
                          val token= pref!!.getString("token","token")
                          user.device_token=token
                          val call= RetrofitService.endpoint.updateToken(user)
                          call.enqueue(object: Callback<String> {
                              override fun onResponse(call: Call<String>?, response:
                              Response<String>?) {
                                  Log.d("test", "user token "+user.device_token)
                                  //   Toast.makeText(ctx,response.toString(),Toast.LENGTH_SHORT).show()

                              }
                              override fun onFailure(call: Call<String>?, t: Throwable?) {
                                  //Toast.makeText(ctx,"failure",Toast.LENGTH_SHORT).show()

                              }
                          })

                          ctx!!.startActivity(ctx!!.intentFor<Main2Activity>())
                          ctx.finish()

                          // view.findNavController().navigate(R.id.action_loginFragment_to_accueilFragment)
                      }
                      else{
                          Toast.makeText(ctx, "Vérifiez le mot de passe", Toast.LENGTH_SHORT).show()

                      }

                  }
                  else{
                      Toast.makeText(ctx, "Vérifiez le numéro de téléphone", Toast.LENGTH_SHORT).show()
                  }


              }
              override fun onFailure(call: Call<List <User>>?, t: Throwable?) {
                  Toast.makeText(ctx,t.toString(),Toast.LENGTH_SHORT).show()

              }
          })

      }

}