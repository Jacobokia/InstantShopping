package com.cobs.instantshopping.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cobs.instantshopping.UserModel
import com.cobs.instantshopping.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var filePath:String
    private lateinit var storageRef: StorageReference
    private lateinit var realTimeDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val storageRef = FirebaseStorage.getInstance()


        val progressBar = binding.progressBar
        val progressBar1 = binding.progressBar1


        registerClickEvents(

        )
    }

    private fun registerClickEvents() {
        binding.signUpBtn.setOnClickListener {
            uploadProfileInfo()
        }
    }

    private fun uploadProfileInfo(){
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance()

        val firstNameId = binding.firstName
        val lastNameId = binding.lastName
        val emailId = binding.email
        val passwordId = binding.password
        val phoneId = binding.phone

        when{

            firstNameId.text!!.isEmpty() -> {}
            lastNameId.text!!.isEmpty() -> {}
            emailId.text!!.isEmpty() -> {}
            passwordId.text!!.isEmpty() -> {}
            phoneId.text!!.isEmpty() ->{}

            else ->{
                val firstName = firstNameId.text.toString()
                val lastName = lastNameId.text.toString()
                val email = emailId.text.toString()
                val password = passwordId.text.toString()
                val phone = phoneId.text.toString()

                val credential = auth.createUserWithEmailAndPassword(email, password)
                credential.addOnSuccessListener {
                    val userId = it.user?.uid ?:"IdNull"
                    val user = UserModel(userId, firstName, lastName, email, password, phone)
                    db.reference.child("users").child(userId).setValue(user).addOnSuccessListener {
                        if (user.position =="Owner"){
                            startActivity(Intent(this, OwnerActivity::class.java))

                        }else{
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
                    Toast.makeText(this, "An error occured, ${it.message}", Toast.LENGTH_LONG).show()
                }

            }

        }
    }

//    private fun uploadProfileImage(){
//        storageRef = storageRef.child(System.currentTimeMillis().toString())
//        imageUri?.let {
//            storageRef.putFile(it).addOnCompleteListener { task->
//                if (task.isSuccessful){
//                    storageRef.downloadUrl.addOnSuccessListener {uri->
//                        val map = HashMap<String, Any>()
//                        map["pic"] = uri.toString()
//
//
//                    }
//                }else{
//                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
//
//    }


}