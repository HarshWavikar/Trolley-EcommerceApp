package com.harshcode.trolley.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.harshcode.trolley.adapter.CategoryProductAdapter
import com.harshcode.trolley.databinding.ActivityCategoryBinding
import com.harshcode.trolley.model.Product

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts(intent.getStringExtra("cate"))
    }

    private fun getProducts(category: String?) {
        val list = ArrayList<Product>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category).get().addOnSuccessListener {
            list.clear()
            for (doc in it.documents){
                val data = doc.toObject(Product::class.java)
                list.add(data!!)
            }
            binding.productByCategoryRV.adapter = CategoryProductAdapter(this, list)
        }
    }
}