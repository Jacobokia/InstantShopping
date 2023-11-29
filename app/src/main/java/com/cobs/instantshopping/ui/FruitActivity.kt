package com.cobs.instantshopping.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityFruitBinding
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FruitActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFruitBinding
    private lateinit var fruitList:ArrayList<Fruit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddFruits.setOnClickListener {
            startActivity(Intent(this, AddFruitsActivity::class.java))
        }
        val fruitRv = binding.fruitRv
        fruitRv.layoutManager = LinearLayoutManager(this)

        fruitList = arrayListOf()
        val db = FirebaseDatabase.getInstance()

        db.reference.child("fruits").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val image = dataSnapshot.getValue(Fruit::class.java)
                        fruitList.add(image!!)

                    }

               fruitRv.adapter = FruitAdapter(this@FruitActivity,fruitList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@FruitActivity,"failed",Toast.LENGTH_SHORT).show()
            }

        })
    }
}