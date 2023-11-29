package com.cobs.instantshopping.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityMeatBinding

class MeatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMeatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddMeat.setOnClickListener {
            startActivity(Intent(this, AddMeatActivity::class.java))
        }
    }
}