package com.cobs.instantshopping.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityBreadBinding
import com.cobs.instantshopping.model.Bread
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BreadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreadBinding
    private lateinit var breadList: ArrayList<Bread>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddBread.setOnClickListener {
            startActivity(Intent(this, AddBreadActivity::class.java))
        }

        val breadRv = binding.breadRv
        breadRv.layoutManager = LinearLayoutManager(this)

        breadList = arrayListOf()
        val db = FirebaseDatabase.getInstance()

        db.reference.child("breads").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val image = dataSnapshot.getValue(Bread::class.java)
                        breadList.add(image!!)

                    }

                    breadRv.adapter = BreadAdapter(this@BreadActivity, breadList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BreadActivity, "failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
