package com.example.mypharma.Views


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.example.mypharma.Controllers.CommandesController
import com.example.mypharma.Models.BraintreeTransaction
import com.example.mypharma.R
import com.example.mypharma.Retrofit.Endpoint
import com.example.mypharma.Retrofit.IBraintreeAPI
import com.example.mypharma.Retrofit.RetrofitClient
import com.example.mypharma.Retrofit.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commande_details.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Retrofit
import android.R.attr.data
import android.util.Log
import com.example.mypharma.Models.BraintreeRequest
import com.example.mypharma.Models.Commande
import com.example.mypharma.Models.Pharmacie
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandeDetailsFragment : Fragment() {
    private val REQUEST_CODE: Int= 1234
    internal  var token :String?=null
    internal  var compositeDisposable= CompositeDisposable()
    internal  lateinit var myAPI: IBraintreeAPI
    lateinit var cmd_detail: Commande

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commande_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cmd_id= arguments?.getInt("commande_id",0)
        val cmdctrl= CommandesController()
        var cmd: Commande?= null
        val call = RetrofitService.endpoint.getCommandeById(cmd_id!!)
        call.enqueue(object : Callback<Commande> {
            override fun onResponse(call: Call<Commande>?, response: Response<Commande>?) {
                progressBar5.visibility = View.GONE
                if(response?.isSuccessful!!) {
                     cmd = response?.body()
                    cmd_detail=cmd!!
                   cmdctrl.displayDatail(activity!!,cmd!!)
                }
                else {
                    toast(response.toString())

                }


            }

            override fun onFailure(call: Call<Commande>?, t: Throwable?) {
                progressBar5.visibility = View.GONE
                toast(t.toString())

            }
        })

       // cmdctrl.loadDetail(activity!!,cmd_id!!)
        myAPI=RetrofitClient.instance.create(IBraintreeAPI::class.java)
        payer.setOnClickListener { submitPayment() }
        compositeDisposable.add(myAPI.token.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({braintreeToken->
                if (braintreeToken.success){
                    token=braintreeToken.clientToken
                }
            },
            {throwable->
                Log.d("PAY","from get token"+throwable!!.message.toString())
            })

        )
    }

    private fun submitPayment() {
            val dropInRequest= DropInRequest().clientToken(token)
            startActivityForResult(dropInRequest.getIntent(this.context),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       // val fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout7)
        //fragment.onActivityResult(requestCode, resultCode, data)

        if (requestCode==REQUEST_CODE){
            if ( resultCode== Activity.RESULT_OK){
                val result= data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce= result.paymentMethodNonce!!.nonce
                if (!TextUtils.isEmpty(amount.toString())){
                    val req= BraintreeRequest(amount.text.toString(),nonce)
                    compositeDisposable.add(myAPI.submitPayment(req)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            t:BraintreeTransaction?->
                            if (t!!.success){
                                cmd_detail.etat="payée"
                                val call = RetrofitService.endpoint.updateCommande(cmd_detail!!)
                                call.enqueue(object : Callback<String> {
                                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                                        if (response?.isSuccessful!!) {
                                            payer.visibility=View.INVISIBLE
                                            etat.text="payée"

                                        } else {

                                        }

                                    }

                                    override fun onFailure(call: Call<String>?, t: Throwable?) {

                                    }
                                })
                                Log.d("PAY","from submit"+t!!.transaction!!.id.toString())
                                //toast(t!!.transaction!!.id.toString())
                            }
                        },{t: Throwable?->Log.d("PAY","from submit"+t!!.message.toString())
                        }))
                }
            }
        }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}
