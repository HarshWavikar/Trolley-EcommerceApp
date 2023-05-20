package com.harshcode.trolley.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDAO {

    @Insert
    suspend fun insertProduct(productR: ProductR)

    @Delete
    suspend fun deleteProduct(productR: ProductR)

    @Query("SELECT * FROM products")
    fun getAllProduct(): LiveData<List<ProductR>>

    @Query("SELECT * FROM products WHERE productID = :id")
    fun isExist(id: String): ProductR
}