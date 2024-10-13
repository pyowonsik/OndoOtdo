package com.wspyo.ondootdo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R
import com.wspyo.ondootdo.model.weather.Wear


class WearRVAdapter(private val items: MutableList<Wear>)
    : RecyclerView.Adapter<WearRVAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WearRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wears_rv_item, parent, false)
        return WearRVAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = view.findViewById(R.id.wear_rv_item)

        fun bindItems(wear: Wear) {
            imageView.setImageResource(wear.imageUrl)
        }
    }

}