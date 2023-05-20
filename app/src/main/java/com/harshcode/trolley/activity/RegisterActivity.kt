package com.harshcode.trolley.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harshcode.trolley.R
import com.harshcode.trolley.databinding.ActivityRegisterBinding
import com.harshcode.trolley.model.UserModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegisterSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegisterSignUp.setOnClickListener {
            validateInput()
        }


    }

    private fun validateInput() {
        if (binding.edtUserName.text!!.isEmpty()) {
            binding.edtUserName.error = "Empty"
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show()
        } else if (binding.edtUserPhoneNumber.text!!.isEmpty()) {
            binding.edtUserPhoneNumber.error = "Empty"
//                Toast.makeText(this, "Please provide mobile number", Toast.LENGTH_SHORT).show()
        } else {
            storeData()
        }
    }

    private fun storeData() {
        val builder = AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please Wait")
            .setCancelable(false)
            .create()
        builder.show()

//Shared preference to store UserName and Number
        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putString("number", binding.edtUserPhoneNumber.text.toString())
        editor.putString("name", binding.edtUserName.text.toString())
        editor.apply()

        //To set data I have used HashMap
//        val data = hashMapOf<String, Any>()
//        data["name"] = binding.edtUserName.text.toString()
//        data["number"] = binding.edtUserPhoneNumber.text.toString()

        val data = UserModel(binding.edtUserName.text.toString(), binding.edtUserPhoneNumber.text.toString())

        Firebase.firestore.collection("users").document(binding.edtUserPhoneNumber.text.toString())
            .set(data).addOnSuccessListener {
                builder.dismiss()
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()

                // As the user is successfully created we open login activity
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                builder.dismiss()
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}