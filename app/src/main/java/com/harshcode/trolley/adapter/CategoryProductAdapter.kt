package com.harshcode.trolley.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshcode.trolley.activity.ProductDetailActivity
import com.harshcode.trolley.databinding.ItemCategoryProductLayoutBinding
import com.harshcode.trolley.model.Product

class CategoryProductAdapter(private val context: Context, private val list: ArrayList<Product>) :
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {

    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
       val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.imgCategoryProductCover)
        holder.binding.tvCategoryProductName.text = list[position].productName
        holder.binding.tvCategoryProductSP.text = list[position].productSP

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productId)
            context.startActivity(intent)
        }
    }
}
