package com.cobs.instantshopping.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cobs.instantshopping.databinding.ActivityAddFruitsBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddFruitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFruitsBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var filePath:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFruitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun registerClickEvents() {
        binding.saveFruitBtn.setOnClickListener {
            saveFruitImage()
        }

        binding.fruitImage.setOnClickListener {
            pickFruitImage.launch("image/*")
        }

    }

    private val pickFruitImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.fruitImage.setImageURI(it)

    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("fruits")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }


    private fun saveFruitImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("images").add(map)
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
                        uploadFruitInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadFruitInfo() {
        val db = FirebaseDatabase.getInstance()

        val name = binding.fruitName.text.toString()
        val image = filePath
        val price = binding.fruitPrice.text.toString()
        val description = binding.fruitDescription.text.toString()
        val quantity = binding.fruitQuantity.text.toString()
        val category = binding.fruitCategory.text.toString()

        val uuid = UUID.randomUUID().toString()

        val fruit = Fruit(name, price, description, quantity, category, image)

        db.reference.child("fruits").child(uuid).setValue(fruit).addOnSuccessListener {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
            binding.fruitName.text?.clear()
            binding.fruitPrice.text?.clear()
            binding.fruitDescription.text?.clear()
            binding.fruitQuantity.text?.clear()
            binding.fruitCategory.text?.clear()


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload product", Toast.LENGTH_SHORT).show()
        }

    }
}