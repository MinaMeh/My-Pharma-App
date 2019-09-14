package com.example.mypharma.Models

class Transaction(
    var id: String?=null,
    var status : String?= null,
    var type:String?=null,
    var currencyIsoCode:String?=null,
    var amount: String?=null,
    var merchantAccountId: String?=null,
    var subMerchantAccountId: String?= null,
    var masterMerchantAccountid:String?=null,
    var orderId: String?=null,
    var createAt: String?=null,
    var updateAt: String?=null
)