package com.cobs.instantshopping.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cobs.instantshopping.model.UserModel
import com.cobs.instantshopping.databinding.ActivityOwnerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OwnerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOwnerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fruitCardView.setOnClickListener {
            startActivity(Intent(this, FruitActivity::class.java))
        }

        binding.breadCardView.setOnClickListener {
            startActivity(Intent(this, BreadActivity::class.java))
        }

        val ownerName = binding.ownerName
        val ownerRole = binding.ownerRole

        val auth = FirebaseAuth.getInstance()
        val uid = auth.uid?:"IdNull"

        val db  = FirebaseDatabase.getInstance()



        db.reference.child("users").child(uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data  = snapshot.getValue(UserModel::class.java)

                ownerName.text = data?.firstName
                ownerRole.text = data?.position
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}