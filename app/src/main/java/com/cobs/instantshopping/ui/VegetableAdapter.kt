package com.cobs.instantshopping.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.model.Vegetable
import com.squareup.picasso.Picasso

class VegetableAdapter(var context: Context, var vegetableList: ArrayList<Vegetable>):RecyclerView.Adapter<VegetableAdapter.VegetableViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VegetableViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.vegetable_item_layout,parent,false)
        return VegetableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vegetableList.size
    }

    override fun onBindViewHolder(holder: VegetableViewHolder, position: Int) {
        holder.vegetableName.text = vegetableList[position].name
        holder.vegetablePrice.text = vegetableList[position].price
        Picasso.get().load(vegetableList[position].image).into(holder.vegetablePic)
    }

    class VegetableViewHolder(view: View):RecyclerView.ViewHolder(view){
        val vegetableName = view.findViewById<TextView>(R.id.vegetable_item_name)
        val vegetablePrice = view.findViewById<TextView>(R.id.vegetable_item_price)
        val vegetablePic = view.findViewById<ImageView>(R.id.vegetable_item_image)

    }
}