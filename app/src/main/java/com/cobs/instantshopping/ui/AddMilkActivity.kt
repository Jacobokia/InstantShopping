package com.cobs.instantshopping.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityAddMeatBinding
import com.cobs.instantshopping.databinding.ActivityAddMilkBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddMilkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMilkBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMilkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("milk")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun registerClickEvents() {
        binding.saveMilkImage.setOnClickListener {
            saveMilkImage()
        }

        binding.milkImage.setOnClickListener {
            pickMilkImage.launch("image/*")
        }
    }

    private val pickMilkImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.milkImage.setImageURI(it)

    }
    private fun saveMilkImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("milk_images").add(map)
                            .addOnCompleteListener { firestoreTask ->

                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Uploaded successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        firestoreTask.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        filePath = uri.toString()
                        uploadMilkInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadMilkInfo() {
        val db = FirebaseDatabase.getInstance()

        val name = binding.milkName.text.toString()
        val image = filePath
        val price = binding.milkPrice.text.toString()
        val description = binding.milkDescription.text.toString()
        val quantity = binding.milkQuantity.text.toString()
        val category = binding.milkCategory.text.toString()

        val uuid = UUID.randomUUID().toString()

        val fruit = Fruit(name, price, description, quantity, category, image)

        db.reference.child("meats").child(uuid).setValue(fruit).addOnSuccessListener {
            Toast.makeText(this, "Milk added successfully", Toast.LENGTH_SHORT).show()
            binding.milkName.text?.clear()
            binding.milkPrice.text?.clear()
            binding.milkDescription.text?.clear()
            binding.milkQuantity.text?.clear()
            binding.milkCategory.text?.clear()

            startActivity(Intent(this@AddMilkActivity,MilkActivity::class.java))


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload Milk", Toast.LENGTH_SHORT).show()
        }

    }
}