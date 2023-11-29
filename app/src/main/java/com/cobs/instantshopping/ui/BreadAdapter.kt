package com.cobs.instantshopping.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cobs.instantshopping.R
import com.cobs.instantshopping.model.Bread
import com.squareup.picasso.Picasso

class BreadAdapter(var context: Context, private var breadList:ArrayList<Bread> ):RecyclerView.Adapter<BreadAdapter.BreadViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreadViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bread_item_layout,parent,false)
        return BreadViewHolder(view)
    }

    override fun getItemCount(): Int {
       return breadList.size
    }

    override fun onBindViewHolder(holder: BreadViewHolder, position: Int) {
        holder.breadName.text = breadList[position].name
        holder.breadPrice.text = breadList[position].price
        Picasso.get().load(breadList[position].image).into(holder.breadPic)
    }

    class BreadViewHolder(view: View):RecyclerView.ViewHolder(view){
        val breadName = view.findViewById<TextView>(R.id.bread_item_name)
        val breadPrice = view.findViewById<TextView>(R.id.bread_item_price)
        val breadPic = view.findViewById<ImageView>(R.id.bread_item_image)

    }
}