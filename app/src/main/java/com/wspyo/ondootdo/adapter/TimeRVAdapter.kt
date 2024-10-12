package com.wspyo.ondootdo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R
import com.wspyo.ondootdo.entity.TimeEntity
class TimeRVAdapter(
    private val items: MutableList<TimeEntity>,
    private val context: Context
) : RecyclerView.Adapter<TimeRVAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.TimeArea)
        val alarmButton: ImageButton = view.findViewById(R.id.AlarmBtn)
//        val alarmSwitch : Switch = view.findViewById(R.id.alarmSwitch)

        fun bindItems(timeEntity: TimeEntity) {
            textView.text = timeEntity.time
            if(timeEntity.isEnabled) {
                alarmButton.setImageResource(R.drawable.toggle_on)
//                alarmSwitch.isChecked = true
            }
            else {
                alarmButton.setImageResource(R.drawable.toggle_off)
//                alarmSwitch.isChecked = false
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])

        // 버튼 클릭 이벤트 처리
        holder.alarmButton.setOnClickListener {
            itemClick?.onClick(it, position)
        }
    }
}