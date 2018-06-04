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


class RecyclerViewAdapterSet(var items : ArrayList<Brick>, var clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapterSet.ViewHolder>() {
    var valuesList = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.set_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.brickNameTextView?.text =  items[position].itemID
        holder?.brickNumberTextView?.text = "#" + position.toString()
        holder?.countTextView?.text = "${items[position].qtyInStore} / ${items[position].qty}"
        valuesList.add(items[position].qtyInStore.toInt())
        holder?.plus?.setOnClickListener { v : View ->
            if (valuesList[position] < items[position].qty.toInt()) {
                valuesList[position] += 1
                if (valuesList[position] == items[position].qty.toInt()) {
                    Log.e("BLABLA1", "$position")
                    holder?.myCV.setCardBackgroundColor(Color.parseColor("#33cc00"))
                }
                else {
                    Log.e("BLABLA2", "$position")
                    holder?.myCV.setCardBackgroundColor(Color.parseColor("#ffffff"))
                }
            }

            holder?.countTextView?.text = valuesList[position].toString() + " / ${items[position].qty}"
        }
        holder?.minus?.setOnClickListener { v : View ->
            if (valuesList[position] > 0) {
                valuesList[position] -= 1
                holder?.myCV.setCardBackgroundColor(Color.parseColor("#ffffff"))
            }
            holder?.countTextView?.text = valuesList[position].toString() + " / ${items[position].qty}"
        }


        /*
        holder?.myNumberTextView?.text = items[position].first
        holder?.myNameTextView?.text = items[position].second

        holder?.itemView.setOnClickListener { clickListener(position) }
        //holder?.containerView?.setOnClickListener { clickListener(item)
        */
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