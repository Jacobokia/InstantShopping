package com.cobs.instantshopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cobs.instantshopping.databinding.FragmentShopBinding
import com.cobs.instantshopping.model.Bread
import com.cobs.instantshopping.model.Fruit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShopFragment : Fragment() {

private var _binding:FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var fruitList: ArrayList<Fruit>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        fruitList = arrayListOf()
        val db = FirebaseDatabase.getInstance()

        db.reference.child("fruits").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val image = dataSnapshot.getValue(Fruit::class.java)
                            fruitList.add(image!!)

                        }

                }
                    withContext(Dispatchers.Main){
                        val fruitRv = binding.fruit
                         fruitRv.adapter = FruitAdapter(requireContext(), fruitList)
                        fruitRv.setHasFixedSize(true)
                         fruitRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun uploadProfilePic(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}