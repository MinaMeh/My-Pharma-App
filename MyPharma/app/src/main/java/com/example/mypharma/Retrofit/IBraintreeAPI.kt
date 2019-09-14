package com.example.mypharma.Retrofit

import com.example.mypharma.Models.BraintreeRequest
import com.example.mypharma.Models.BraintreeToken
import com.example.mypharma.Models.BraintreeTransaction
import io.reactivex.Observable
import retrofit2.http.*

interface IBraintreeAPI {
    @get: GET("/checkouts/new")
    val token: Observable<BraintreeToken>

    @POST("/checkouts")
    fun submitPayment(@Body req: BraintreeRequest): Observable<BraintreeTransaction>

}