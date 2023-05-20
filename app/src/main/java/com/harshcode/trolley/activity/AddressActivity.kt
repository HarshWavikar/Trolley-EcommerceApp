package com.harshcode.trolley.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.R
import com.harshcode.trolley.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var preference : SharedPreferences

    private lateinit var totalCost : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = this.getSharedPreferences("user", MODE_PRIVATE)

        totalCost = intent.getStringExtra("totalCost")!!
        loadUserInfo()
        binding.btnProceedToCheckout.setOnClickListener {
            validateData(
                binding.edtUserNameAddress.text.toString(),
                binding.edtUserPhoneNumberAddress.text.toString(),
                binding.edtVillageAddress.text.toString(),
                binding.edtCityAddress.text.toString(),
                binding.edtStateAddress.text.toString(),
                binding.edtPinCodeAddress.text.toString()
            )
        }
    }

    private fun validateData(
        userName: String,
        number: String,
        village: String,
        city: String,
        state: String,
        pinCode: String
    ) {
        if (userName.isEmpty() || number.isEmpty() || village.isEmpty() || city.isEmpty() || state.isEmpty() || pinCode.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }else{
            //Here we are updating the users address info
            storeData(village, city, state, pinCode)
        }
    }

    //
    private fun storeData(village: String, city: String, state: String, pinCode: String) {
val map = hashMapOf<String, Any>()
        map["village"] = village
        map["state"] = state
        map["city"] = city
        map["pinCode"] = pinCode

        Firebase.firestore.collection("users")
            .document(preference.getString("number", "")!!)
            .update(map)
            .addOnSuccessListener {
                val b = Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                b.putString("totalCost", totalCost)
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
            startActivity(intent)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadUserInfo() {

        Firebase.firestore.collection("users")
            .document(preference.getString("number", "")!!)
            .get().addOnSuccessListener {
                binding.edtUserNameAddress.setText(it.getString("userName"))
                binding.edtUserPhoneNumberAddress.setText(it.getString("userPhoneNumber"))
                binding.edtVillageAddress.setText(it.getString("village"))
                binding.edtStateAddress.setText(it.getString("state"))
                binding.edtCityAddress.setText(it.getString("city"))
                binding.edtPinCodeAddress.setText(it.getString("pinCode"))
            }
            .addOnFailureListener {

            }
    }
}