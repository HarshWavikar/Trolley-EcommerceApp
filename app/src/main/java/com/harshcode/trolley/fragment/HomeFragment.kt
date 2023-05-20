package com.harshcode.trolley.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.R
import com.harshcode.trolley.adapter.CategoryAdapter
import com.harshcode.trolley.adapter.ProductAdapter
import com.harshcode.trolley.databinding.FragmentHomeBinding
import com.harshcode.trolley.model.CategoryModel
import com.harshcode.trolley.model.Product

class HomeFragment : Fragment() {

    private var _Binding: FragmentHomeBinding? = null
    private val binding get() = _Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _Binding = FragmentHomeBinding.inflate(inflater, container, false)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if (preference.getBoolean("isCart", false))
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        getSliderImage()
        getProductCategory()
        getProducs()
        return binding.root
    }

    private fun getSliderImage() {
        Firebase.firestore.collection("slider").document("item").get().addOnSuccessListener {
            Glide.with(requireContext()).load(it.get("img")).into(binding.imgSlider)
        }
    }

    private fun getProductCategory() {
        //Here we initialize the list
        val list = ArrayList<CategoryModel>()
        Firebase.firestore.collection("category").get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val data = doc.toObject(CategoryModel::class.java)
                list.add(data!!)
            }
            val adapter = CategoryAdapter(requireContext(),list)
            binding.categoryRV.adapter = adapter
        }
    }

    private fun getProducs() {
        //Here we initialize the list
        val list = ArrayList<Product>()
        Firebase.firestore.collection("products").get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){
                val data = doc.toObject(Product::class.java)
                list.add(data!!)
            }
            val adapter = ProductAdapter(requireContext(),list)
            binding.productRV.adapter = adapter
        }
    }
}