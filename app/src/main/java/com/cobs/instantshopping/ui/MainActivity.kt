package com.cobs.instantshopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cobs.instantshopping.R
import com.cobs.instantshopping.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(ShopFragment())

        binding.bottomNavigationView.setOnItemReselectedListener {
            when(it.itemId){
                R.id.shop -> replaceFragment(ShopFragment())
                R.id.account ->replaceFragment(AccountFragment())
                R.id.cart ->replaceFragment(CartFragment())
                R.id.my_order ->replaceFragment(MyOrderFragment())

                else ->{

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }
}