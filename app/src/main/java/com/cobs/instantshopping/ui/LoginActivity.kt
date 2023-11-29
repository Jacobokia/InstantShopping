package com.cobs.instantshopping.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cobs.instantshopping.UserModel
import com.cobs.instantshopping.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseDatabase.getInstance()

        val emailId = binding.email
        val passwordId = binding.password

        val progressBar = binding.progressBar
        val progressBar1 = binding.progressBar1

        val signInButton = binding.signIn


        binding.navigateToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signInButton.setOnClickListener {

            when{
                emailId.text!!.isEmpty() -> {
                    showDialog("Invalid input", "You must input your email")
                }
                passwordId.text!!.isEmpty() -> {
                    showDialog("Invalid input", "You must input your password")
                }

                else -> {
                    progressBar.visibility = View.VISIBLE
                    progressBar1.visibility = View.VISIBLE

                        val email = emailId.text.toString()
                        val password = passwordId.text.toString()

                        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                            val uid = it.user?.uid ?:"IdNull"

                            db.reference.child("users").child(uid).addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val key = snapshot.key
                                    Log.d("Snap", "${snapshot.value}")

                                    val data = snapshot.getValue(UserModel::class.java)
                                    Log.d("Key", "$key")
                                    Log.d("Data P", "${data?.position}")
                                    Log.d("Data N", "${data?.firstName}")


                                        if (data?.position == "Owner"){
                                            startActivity(Intent(this@LoginActivity, OwnerActivity::class.java))

                                        }else{
                                          startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                        }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("Error", "${error.message}")
                                }

                            })
                        }.addOnFailureListener {
                            Toast.makeText(this, "An error occurred: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }



                }
            }
        }

    private fun showDialog(title:String, message:String){
        val alert = AlertDialog.Builder(this).setTitle(title).setMessage(message)
            .setNegativeButton("Cancel"){ p0,p1-> p0?.dismiss()}
        alert.create()
        alert.show()
    }


}