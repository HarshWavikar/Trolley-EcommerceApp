package com.harshcode.trolley.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.MainActivity
import com.harshcode.trolley.R
import com.harshcode.trolley.roomdb.AppDatabase
import com.harshcode.trolley.roomdb.ProductR
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_test_fZ4nYwPRcD1jME")

        val price =  intent.getStringExtra("totalCost")
        try {
            val options = JSONObject()
            options.put("name", "Trolley")
            options.put("description", "Best Ecommerce App")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#8A4CD6")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt() * 100)) //pass amount in currency subunits
            options.put("prefill.email", "harshwavikar123@gmail.com")
            options.put("prefill.contact", "9930255206")

            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        // when our payment is successful, the data will be stored in firestore
        uploadData()
    }

    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productIds")

        //Here we are getting Id's of all the items in the cart, and storing this data on the server i.e on Firestore database
        for (currentId in id!!) {
            // Before uploading the data to fireStore, we should have the details of the products, so here we fetch data of each product
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()
        Firebase.firestore.collection("products")
            .document(productId!!)
            .get()
            .addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO){
                    dao.deleteProduct(ProductR(productId,))
                }
            saveData(it.getString("productName"), it.getString("productSP"), productId)
        }

    }

    private fun saveData(productName: String?, productSP: String?, productId: String) {
        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["productName"] = productName!!
        data["productSP"] = productSP!!
        data["productId"] = productId
        // Also we want some other information :-
        data["status"] = "Ordered"
        data["userId"] = preferences.getString("number", "")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        //Here we set the data above in fireStore
        //Same key is used as document and orderId
        firestore.document(key).set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }


    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
    }
}