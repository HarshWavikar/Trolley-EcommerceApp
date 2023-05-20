package com.harshcode.trolley.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Api.ApiOptions.HasOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.databinding.AllOrderItemLayoutBinding
import com.harshcode.trolley.model.AllOrderModel


class AllOrderAdapter(private val context: Context, private val list: ArrayList<AllOrderModel>) :
    RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    inner class AllOrderViewHolder(val binding: AllOrderItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        val binding =
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return AllOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.tvProductTitle.text = list[position].productName
        holder.binding.tvProductPrice.text = list[position].productSP


        //Here we want to know the status of the order
        when (list[position].status) {

            "Ordered" -> {
                holder.binding.tvProductStatus.text = "Ordered"
            }
            "Dispatched" -> {
                holder.binding.tvProductStatus.text = "Dispatched"

            }
            "Delivered" -> {
                holder.binding.tvProductStatus.text = "Delivered"

            }
            "Cancelled" -> {
                holder.binding.tvProductStatus.text = "Cancelled by the Seller"

            }
        }
    }
}



















