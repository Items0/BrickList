package com.example.patryk.bricklist_project

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView


class RecyclerViewAdapter(val items : ArrayList<String>, val clickListener: (String, Int) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_item_layout, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.myTextView?.text = items[position]

        holder?.itemView.setOnClickListener { clickListener(items[position], position) }
        //holder?.containerView?.setOnClickListener { clickListener(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myTextView = itemView.findViewById<TextView>(R.id.name)
    }

}