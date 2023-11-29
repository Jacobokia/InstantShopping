package com.cobs.instantshopping.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityAddMeatBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddMeatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMeatBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meat)

        initVars()
        registerClickEvents()
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("meats")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun registerClickEvents() {
        binding.saveMeatImage.setOnClickListener {
            saveMeatImage()
        }

        binding.meatImage.setOnClickListener {
            pickMeatImage.launch("image/*")
        }
    }

    private val pickMeatImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.meatImage.setImageURI(it)

    }

    private fun saveMeatImage() {
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("meat_images").add(map)
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
                        uploadMeatInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadMeatInfo() {
        val db = FirebaseDatabase.getInstance()

        val name = binding.meatName.text.toString()
        val image = filePath
        val price = binding.meatPrice.text.toString()
        val description = binding.meatDescription.text.toString()
        val quantity = binding.meatQuantity.text.toString()
        val category = binding.meatCategory.text.toString()

        val uuid = UUID.randomUUID().toString()

        val fruit = Fruit(name, price, description, quantity, category, image)

        db.reference.child("meats").child(uuid).setValue(fruit).addOnSuccessListener {
            Toast.makeText(this, "Meat added successfully", Toast.LENGTH_SHORT).show()
            binding.meatName.text?.clear()
            binding.meatPrice.text?.clear()
            binding.meatDescription.text?.clear()
            binding.meatQuantity.text?.clear()
            binding.meatCategory.text?.clear()

            startActivity(Intent(this@AddMeatActivity,MeatActivity::class.java))


        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload Meat", Toast.LENGTH_SHORT).show()
        }

    }
}