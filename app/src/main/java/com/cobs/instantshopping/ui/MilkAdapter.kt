package com.cobs.instantshopping.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.model.Milk
import com.squareup.picasso.Picasso

class MilkAdapter(var context: Context, var milkList: ArrayList<Milk>):RecyclerView.Adapter<MilkAdapter.MilkViewHoder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilkViewHoder {
        val view = LayoutInflater.from(context).inflate(R.layout.milk_item_layout,parent,false)
        return MilkViewHoder(view)
    }

    override fun getItemCount(): Int {
        return milkList.size
    }

    override fun onBindViewHolder(holder: MilkViewHoder, position: Int) {
        holder.milkName.text = milkList[position].name
        holder.milkPrice.text = milkList[position].price
        Picasso.get().load(milkList[position].image).into(holder.milkPic)
    }

    class MilkViewHoder(view: View):RecyclerView.ViewHolder(view){
        val milkName = view.findViewById<TextView>(R.id.milk_item_name)
        val milkPrice = view.findViewById<TextView>(R.id.milk_item_price)
        val milkPic = view.findViewById<ImageView>(R.id.milk_item_image)

    }
}