package com.example.patryk.bricklist_project

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView


class RecyclerViewAdapterMain(var items : ArrayList<Pair<String, String>>, val clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_item_layout, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.myNumberTextView?.text = items[position].first
        holder?.myNameTextView?.text = items[position].second

        holder?.itemView.setOnClickListener { clickListener(position) }
        //holder?.containerView?.setOnClickListener { clickListener(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myNumberTextView = itemView.findViewById<TextView>(R.id.brickNumberTextView)
        val myNameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
    }

}