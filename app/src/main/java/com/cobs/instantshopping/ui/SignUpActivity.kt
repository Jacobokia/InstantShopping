package com.cobs.instantshopping.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cobs.instantshopping.model.UserModel
import com.cobs.instantshopping.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var filePath: String
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var realTimeDatabase: FirebaseDatabase
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }
    private fun registerClickEvents() {
        binding.signUpBtn.setOnClickListener {
            uploadProfileImage()
        }

        binding.signUpImage.setOnClickListener {
            pickProfileImage.launch("image/*")
        }
    }

    private val pickProfileImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.signUpImage.setImageURI(it)

    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("profile_pics")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }



    private fun uploadProfileImage() {

        val progressBar = binding.progressBar
        val progressBar1 = binding.progressBar1
        progressBar.visibility = View.VISIBLE
        progressBar1.visibility = View.VISIBLE
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()

                        firebaseFirestore.collection("profile_images").add(map)
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
                        uploadProfileInfo()
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun uploadProfileInfo() {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance()

        val firstNameId = binding.firstName
        val image = filePath
        val lastNameId = binding.lastName
        val emailId = binding.email
        val passwordId = binding.password
        val phoneId = binding.phone

        when {

            firstNameId.text!!.isEmpty() -> {}
            lastNameId.text!!.isEmpty() -> {}
            emailId.text!!.isEmpty() -> {}
            passwordId.text!!.isEmpty() -> {}
            phoneId.text!!.isEmpty() -> {}

            else -> {

                val firstName = firstNameId.text.toString()
                val lastName = lastNameId.text.toString()
                val email = emailId.text.toString()
                val password = passwordId.text.toString()
                val phone = phoneId.text.toString()

                val credential = auth.createUserWithEmailAndPassword(email, password)
                credential.addOnSuccessListener {
                    val userId = it.user?.uid ?: "IdNull"
                    val user = UserModel(userId,image,firstName, lastName, email, password, phone)
                    db.reference.child("users").child(userId).setValue(user).addOnSuccessListener {
                        if (user.position == "Owner") {
                            startActivity(Intent(this, OwnerActivity::class.java))

                        } else {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "An error occured,${{ it.message }}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "An error occured, ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }

            }

        }
    }
}