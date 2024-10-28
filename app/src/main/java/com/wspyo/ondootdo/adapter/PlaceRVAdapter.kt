package com.wspyo.ondootdo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R
import com.wspyo.ondootdo.model.local.Document
import com.wspyo.ondootdo.model.weather.Wear

class PlaceRVAdapter(private val items: List<Document>)
    : RecyclerView.Adapter<PlaceRVAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_rv_item, parent, false)
        return PlaceRVAdapter.ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view : View , position: Int)
    }

    var itemClick : ItemClick? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeArea : TextView = view.findViewById(R.id.PlaceArea)
        val addressArea : TextView = view.findViewById(R.id.AddressArea)
        val placeItemArea : ConstraintLayout = view.findViewById(R.id.PlaceItemArea)

        fun bindItems(place: Document) {
            placeArea.text = place.place_name
            addressArea.text = place.address_name
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])

        holder.placeItemArea.setOnClickListener{
            itemClick?.onClick(it , position)
        }
    }

}