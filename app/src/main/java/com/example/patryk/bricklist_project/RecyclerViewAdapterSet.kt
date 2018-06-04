package com.example.patryk.bricklist_project

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.TextView


class RecyclerViewAdapterSet(var items : ArrayList<Brick>) : RecyclerView.Adapter<RecyclerViewAdapterSet.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.set_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.brickNameTextView?.text =  items[position].itemID
        holder?.brickNumberTextView?.text = "#" + position.toString()
        holder?.countTextView?.text = "${items[position].qtyInStore} / ${items[position].qty}"
        holder?.plus?.setOnClickListener { v : View ->
            if (items[position].qtyInStore.toInt() < items[position].qty.toInt()) {
                items[position].qtyInStore = (items[position].qtyInStore.toInt() + 1).toString()
                if (items[position].qtyInStore == items[position].qty) {
                    holder?.myCV.setCardBackgroundColor(Color.parseColor("#33cc00"))
                }
                else {
                    holder?.myCV.setCardBackgroundColor(Color.parseColor("#ffffff"))
                }
            }
            holder?.countTextView?.text = items[position].qtyInStore + " / ${items[position].qty}"
        }
        holder?.minus?.setOnClickListener { v : View ->
            if (items[position].qtyInStore.toInt() > 0) {
                items[position].qtyInStore = (items[position].qtyInStore.toInt() - 1).toString()
                holder?.myCV.setCardBackgroundColor(Color.parseColor("#ffffff"))
            }
            holder?.countTextView?.text = items[position].qtyInStore + " / ${items[position].qty}"
        }

        // set background color when start
        if (items[position].qtyInStore == items[position].qty) {
            holder?.myCV.setCardBackgroundColor(Color.parseColor("#33cc00"))
        }
        else {
            holder?.myCV.setCardBackgroundColor(Color.parseColor("#ffffff"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brickNumberTextView = itemView.findViewById<TextView>(R.id.brickNumberTextView)
        val brickNameTextView = itemView.findViewById<TextView>(R.id.brickNameTextView)
        val countTextView = itemView.findViewById<TextView>(R.id.countView)
        val plus = itemView.findViewById<Button>(R.id.plusButton)
        val minus = itemView.findViewById<Button>(R.id.minusButton)
        val myCV = itemView.findViewById<CardView>(R.id.myCardView)

    }
}