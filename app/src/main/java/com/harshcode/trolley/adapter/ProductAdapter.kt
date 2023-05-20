package com.harshcode.trolley.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshcode.trolley.activity.ProductDetailActivity
import com.harshcode.trolley.databinding.ItemProductLayoutBinding
import com.harshcode.trolley.model.Product

class ProductAdapter(private val context: Context, private val list: ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

     inner class ProductViewHolder (val binding : ItemProductLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
       val product = list[position]
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.imgProduct)
        holder.binding.tvProductName.text = product.productName
        holder.binding.tvProductCategory.text = product.productCategory
        holder.binding.tvProductMRP.text = product.productMRP

        holder.binding.btnSP.text = product.productSP

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }
}