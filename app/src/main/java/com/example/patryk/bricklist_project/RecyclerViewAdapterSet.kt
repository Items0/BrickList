package com.example.patryk.bricklist_project

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView


class RecyclerViewAdapterSet(val items : ArrayList<Pair<String, String>>, val clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapterSet.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.set_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.brickNumberTextView?.text = items[position].first
        holder?.countTextView?.text = items[position].second

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
        //val myNumberTextView = itemView.findViewById<TextView>(R.id.numberTextView)
        //val myNameTextView = itemView.findViewById<TextView>(R.id.numberTextView)
    }

}