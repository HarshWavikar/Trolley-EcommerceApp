package com.harshcode.trolley.model

data class AllOrderModel(
    val orderId: String? = "",
    val productId: String? = "",
    val productName: String? = "",
    val productSP: String? = "",
    val status: String? = "",
    val userId: String? = "",
)