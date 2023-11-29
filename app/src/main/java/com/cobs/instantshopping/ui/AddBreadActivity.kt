package com.cobs.instantshopping.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cobs.instantshopping.databinding.ActivityAddBreadBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddBreadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBreadBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("breads")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun registerClickEvents() {
        binding.saveBreadImage.setOnClickListener {
            saveBreadImage()
        }

        binding.breadImage.setOnClickListener {
            pickBreadImage.launch("image/*")
        }

    }

    private val pickBreadImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.breadImage.setImageURI(it)

    }

    private fun saveBreadImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("bread_images").add(map)
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
                        uploadBreadInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadBreadInfo() {
        val db = FirebaseDatabase.getInstance()

        val name = binding.breadName.text.toString()
        val image = filePath
        val price = binding.breadPrice.text.toString()
        val description = binding.breadDescription.text.toString()
        val quantity = binding.breadQuantity.text.toString()
        val category = binding.breadCategory.text.toString()

        val uuid = UUID.randomUUID().toString()

        val fruit = Fruit(name, price, description, quantity, category, image)

        db.reference.child("breads").child(uuid).setValue(fruit).addOnSuccessListener {
            Toast.makeText(this, "Bread added successfully", Toast.LENGTH_SHORT).show()
            binding.breadName.text?.clear()
            binding.breadPrice.text?.clear()
            binding.breadDescription.text?.clear()
            binding.breadQuantity.text?.clear()
            binding.breadCategory.text?.clear()

            startActivity(Intent(this@AddBreadActivity,BreadActivity::class.java))


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload bread", Toast.LENGTH_SHORT).show()
        }

    }
}


