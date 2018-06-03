package com.example.patryk.bricklist_project

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView


class RecyclerViewAdapter(val items : ArrayList<Pair<String, String>>, val clickListener: (String, Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_item_layout, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.myNumberTextView?.text = items[position].first
        holder?.myNameTextView?.text = items[position].second

        holder?.itemView.setOnClickListener { clickListener(items[position].first, position) }
        //holder?.containerView?.setOnClickListener { clickListener(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myNumberTextView = itemView.findViewById<TextView>(R.id.numberTextView)
        val myNameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
    }

}