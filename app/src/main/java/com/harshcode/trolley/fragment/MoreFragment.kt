package com.harshcode.trolley.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.R
import com.harshcode.trolley.adapter.AllOrderAdapter
import com.harshcode.trolley.databinding.FragmentMoreBinding
import com.harshcode.trolley.model.AllOrderModel

class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var list: ArrayList<AllOrderModel>
    private lateinit var adapter : AllOrderAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(layoutInflater)

        list = ArrayList()

        val preferences = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)

        //Here we are getting the ordered products of SPECIFIC userId
        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId", preferences.getString("number", "")!!)
            .get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)
                }
                adapter = AllOrderAdapter(requireContext(),list)
                binding.moreRV.adapter = adapter
            }



        return binding.root
    }
}