package com.harshcode.trolley.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.MainActivity
import com.harshcode.trolley.R
import com.harshcode.trolley.databinding.ActivityProductDetailBinding
import com.harshcode.trolley.roomdb.AppDatabase
import com.harshcode.trolley.roomdb.ProductDAO
import com.harshcode.trolley.roomdb.ProductR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Now here i have to set the data which can be done in two ways
        1. taking data from previous activity
        2. taking data from FireStore directly
         */

        getProductDetail(intent.getStringExtra("id"))
    }

    private fun getProductDetail(productID: String?) {

        Firebase.firestore.collection("products").document(productID!!).get().addOnSuccessListener {
            val list = it.get("productImages") as ArrayList<String>
            val name = it.getString("productName")
            val productSP = it.getString("productSP")
            val productDescription = it.getString("productDescription")
            binding.tvProductTitle.text = name
            binding.tvProductSP.text = productSP
            binding.tvProductDescription.text = productDescription

            val imageList = ArrayList<SlideModel>()
            for (data in list) {
                imageList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
            }


            cartAction(productID, name, productSP, it.getString("productCoverImage"))

            binding.imageSlider.setImageList(imageList)

        }.addOnFailureListener {

        }

    }

    private fun cartAction(
        productID: String,
        name: String?,
        productSP: String?,
        coverImage: String?
    ) {

        //Here we have added instance of productDAO
        val productDAO = AppDatabase.getInstance(this).productDao()

        // If this condition is true means product is already added to cart
        //Here we are just changing the text of the button
        if (productDAO.isExist(productID) != null) {
            binding.tvCart.text = "Go To Cart"
        } else {
            binding.tvCart.text = "Add To Cart"
        }

        /*Here we have added click listener to the button,
        if condition is true, that means product is added to cart
        and on clicking we will open the cart else we will add the product to Cart */
        binding.tvCart.setOnClickListener {
            if (productDAO.isExist(productID) != null) {
                openCart()
            } else {
                // This function will add product to cart
                addToCart(productDAO, productID, name, productSP, coverImage)
            }
        }
    }

    private fun addToCart(
        productDAO: ProductDAO,
        productID: String,
        name: String?,
        productSP: String?,
        coverImage: String?
    ) {
        val data = ProductR(productID, name, coverImage, productSP)
        lifecycleScope.launch(Dispatchers.IO) {
            productDAO.insertProduct(data)
            binding.tvCart.text = "Go To Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}