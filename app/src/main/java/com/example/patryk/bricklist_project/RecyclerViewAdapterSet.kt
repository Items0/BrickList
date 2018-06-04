package com.example.patryk.bricklist_project

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.TextView


class RecyclerViewAdapterSet(var items : ArrayList<Pair<String, String>>, var clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapterSet.ViewHolder>() {
    var valuesList = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.set_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.brickNumberTextView?.text = items[position].first
        holder?.countTextView?.text = items[position].second
        valuesList.add(items[position].second.toInt())
        holder?.plus?.setOnClickListener { v : View ->
            if (valuesList[position] < 10) {
                valuesList[position] += 1
            }
            holder?.countTextView?.text = valuesList[position].toString()
        }
        holder?.minus?.setOnClickListener { v : View ->
            if (valuesList[position] > 0) {
                valuesList[position] -= 1
            }
            holder?.countTextView?.text = valuesList[position].toString()
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brickNumberTextView = itemView.findViewById<TextView>(R.id.brickNumberTextView)
        val countTextView = itemView.findViewById<TextView>(R.id.countView)
        val plus = itemView.findViewById<Button>(R.id.plusButton)
        val minus = itemView.findViewById<Button>(R.id.minusButton)
    }

}