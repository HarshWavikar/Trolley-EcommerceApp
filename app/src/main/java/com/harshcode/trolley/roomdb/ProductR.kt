package com.harshcode.trolley.roomdb

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductR(

    @PrimaryKey
    @NonNull
    val productID: String,
    @ColumnInfo("productName") val productName :String? = "",
    @ColumnInfo("productImage") val productImage :String? = "",
    @ColumnInfo("productSP") val productSP : String? = ""
)