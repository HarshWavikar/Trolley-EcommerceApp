package com.harshcode.trolley.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshcode.trolley.activity.ProductDetailActivity
import com.harshcode.trolley.databinding.ItemCartLayoutBinding
import com.harshcode.trolley.roomdb.AppDatabase
import com.harshcode.trolley.roomdb.ProductR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(private val context: Context, private val list: List<ProductR>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    inner class CartViewHolder(val binding : ItemCartLayoutBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
       val binding = ItemCartLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imgCartProduct)
        holder.binding.tvCartProductName.text = list[position].productName
        holder.binding.tvCartProductSP.text = list[position].productSP

        // So when we click on the product visible in the cart detail page should open
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id", list[position].productID )
            context.startActivity(intent)
        }


        val dao = AppDatabase.getInstance(context).productDao()
        holder.binding.btnDelete.setOnClickListener {
            // As delete is a suspended function so I have used coroutines
            GlobalScope.launch(Dispatchers.IO) {
                dao.deleteProduct(ProductR(list[position].productID, list[position].productName, list[position].productImage, list[position].productSP))
            }
        }

    }
}