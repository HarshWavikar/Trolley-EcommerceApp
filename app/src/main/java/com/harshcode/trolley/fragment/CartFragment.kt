package com.harshcode.trolley.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.harshcode.trolley.R
import com.harshcode.trolley.activity.AddressActivity
import com.harshcode.trolley.activity.CategoryActivity
import com.harshcode.trolley.adapter.CartAdapter
import com.harshcode.trolley.databinding.FragmentCartBinding
import com.harshcode.trolley.roomdb.AppDatabase
import com.harshcode.trolley.roomdb.ProductR

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(layoutInflater)


        val preference =
            requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        //This makes sure that when app runs it doesn't open cart fragment but HomeFragment
        editor.putBoolean("isCart", false)
        editor.apply()


        // Now to get data from database
        val dao = AppDatabase.getInstance(requireContext()).productDao()

        //Here i have initialized a list that holds Id of products
        list = ArrayList()
        dao.getAllProduct().observe(requireActivity(), Observer {
            binding.cartRV.adapter = CartAdapter(requireContext(), it)

            list.clear()
            for (data in it) {
                list.add(data.productID)
            }
            totalCost(it)
            if (list.isEmpty()) binding.llEmptyCart.visibility = VISIBLE
        })
        return binding.root
    }

    private fun totalCost(data: List<ProductR>?) {
        var total = 0
        for (item in data!!) {
            total += item.productSP!!.toInt()
        }
        binding.tvTotalItems.text = "Total items in cart is ${data.size}"
        binding.tvTotalCost.text = "Total Cost:  ₹$total"

        binding.btnCheckOut.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)

            val b = Bundle()
            b.putStringArrayList("productIds",list)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }
}