package com.cobs.instantshopping.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.model.Meat
import com.squareup.picasso.Picasso

class MeatAdapter(var context: Context, var meatList: ArrayList<Meat>):RecyclerView.Adapter<MeatAdapter.MeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.meat_item_layout,parent,false)
        return MeatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meatList.size
    }

    override fun onBindViewHolder(holder: MeatViewHolder, position: Int) {
        holder.meatName.text = meatList[position].name
        holder.meatPrice.text = meatList[position].price
        Picasso.get().load(meatList[position].image).into(holder.meatPic)
    }

    class MeatViewHolder(view: View):RecyclerView.ViewHolder(view){
        val meatName = view.findViewById<TextView>(R.id.meat_item_name)
        val meatPrice = view.findViewById<TextView>(R.id.meat_item_price)
        val meatPic = view.findViewById<ImageView>(R.id.meat_item_image)
    }
}