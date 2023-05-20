package com.harshcode.trolley.model

class Product(
    val productName: String? = "",
    val productDescription: String? = "",
    val productMRP: String? = "",
    val productSP: String? = "",
    val productCategory: String? = "",
    val productCoverImage: String? = "",
    val productImages: ArrayList<String> = ArrayList(),
    val productId: String? = "",
)