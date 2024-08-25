package com.wspyo.ondootdo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R

class TimeRVAdapter(val items : List<String>,val context : Context) : RecyclerView.Adapter<TimeRVAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val textView : TextView = view.findViewById(R.id.timeArea)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeRVAdapter.ViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}