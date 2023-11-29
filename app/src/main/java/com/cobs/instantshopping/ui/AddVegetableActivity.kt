package com.cobs.instantshopping.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityAddMilkBinding
import com.cobs.instantshopping.databinding.ActivityAddVegetableBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddVegetableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddVegetableBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVegetableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("vegetables")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun registerClickEvents() {
        binding.saveVegetableImage.setOnClickListener {
            saveVegetableImage()
        }

        binding.vegetableImage.setOnClickListener {
            pickVegetableImage.launch("image/*")
        }
    }

    private val pickVegetableImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.vegetableImage.setImageURI(it)

    }

    private fun saveVegetableImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("vegetable_images").add(map)
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
                        uploadVegetableInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadVegetableInfo() {
        val db = FirebaseDatabase.getInstance()

        val name = binding.vegetableName.text.toString()
        val image = filePath
        val price = binding.vegetablePrice.text.toString()
        val description = binding.vegetableDescription.text.toString()
        val quantity = binding.vegetableQuantity.text.toString()
        val category = binding.vegetableCategory.text.toString()

        val uuid = UUID.randomUUID().toString()

        val fruit = Fruit(name, price, description, quantity, category, image)

        db.reference.child("vegetables").child(uuid).setValue(fruit).addOnSuccessListener {
            Toast.makeText(this, "vegetable added successfully", Toast.LENGTH_SHORT).show()
            binding.vegetableName.text?.clear()
            binding.vegetablePrice.text?.clear()
            binding.vegetableDescription.text?.clear()
            binding.vegetableQuantity.text?.clear()
            binding.vegetableCategory.text?.clear()

            startActivity(Intent(this@AddVegetableActivity,VegetableActivity::class.java))


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload Vegetable", Toast.LENGTH_SHORT).show()
        }

    }
}