package com.cobs.instantshopping.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.model.Fruit
import com.squareup.picasso.Picasso

class FruitAdapter(val context:Context, private var fruitList:ArrayList<Fruit>):RecyclerView.Adapter<FruitAdapter.FruitViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fruit_item_layout,parent,false)
        return FruitViewHolder(view)
    }

    override fun getItemCount(): Int {
     return fruitList.size
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        holder.fruitName.text = fruitList[position].name
        holder.fruitPrice.text = fruitList[position].price
        Picasso.get().load(fruitList[position].image).into(holder.fruitPic)

    }

    class FruitViewHolder(view: View):RecyclerView.ViewHolder(view){
        val fruitName = view.findViewById<TextView>(R.id.fruit_item_name)
        val fruitPrice = view.findViewById<TextView>(R.id.fruit_item_price)
        val fruitPic = view.findViewById<ImageView>(R.id.fruit_item_image)

    }
}