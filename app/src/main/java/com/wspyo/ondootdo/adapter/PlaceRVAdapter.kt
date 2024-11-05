package com.wspyo.ondootdo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R
import com.wspyo.ondootdo.model.local.Document

class PlaceRVAdapter(private var items: List<Document>) : RecyclerView.Adapter<PlaceRVAdapter.ViewHolder>() {

    // 아이템 수 반환
    override fun getItemCount(): Int = items.size

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_rv_item, parent, false)
        return ViewHolder(view)
    }

    // 아이템 클릭 인터페이스 정의
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    // 클릭 리스너
    var itemClick: ItemClick? = null

    // ViewHolder 클래스 정의
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeArea: TextView = view.findViewById(R.id.PlaceArea)
        val addressArea: TextView = view.findViewById(R.id.AddressArea)
        val placeItemArea: ConstraintLayout = view.findViewById(R.id.PlaceItemArea)
        val categoryArea : TextView = view.findViewById(R.id.CategoryArea)

        // 아이템 데이터 바인딩
        fun bindItems(place: Document) {
            placeArea.text = place.place_name
            addressArea.text = place.address_name
            categoryArea.text = place.category_name.split(">")[place.category_name.split(">").size-1]
        }
    }

    // ViewHolder에 데이터 바인딩 및 클릭 리스너 설정
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])

        // 아이템 클릭 시 itemClick 호출
        holder.placeItemArea.setOnClickListener {
            itemClick?.onClick(it, position)
        }
    }

    // 새로운 데이터로 아이템 리스트 업데이트
    fun updateData(newItems: List<Document>) {
        items = newItems
        notifyDataSetChanged()  // 어댑터에 데이터 변경 알림
    }
}
