package com.wspyo.ondootdo.adapter

import WeatherForecast
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.R
import com.wspyo.ondootdo.getCurrentWeather


class WeatherRVAdapter(
    private val items: List<WeatherForecast>,
    private val context: Context
) : RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_rv_item, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val weatherImageArea: ImageView = view.findViewById(R.id.WeatherImageArea)
        private val temperatureTextView: TextView = view.findViewById(R.id.TemperatureTextArea)
        private val weatherTextView : TextView = view.findViewById(R.id.WeatherTextArea)
        private val timeTextView: TextView = view.findViewById(R.id.TimeTextArea)

        fun bindItems(weather: WeatherForecast) {
            // 이미지 이름 가져오기
            val imageName = getCurrentWeather(weather.weather.first().main)["weatherImg"]

            val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            weatherImageArea.setImageResource(resourceId)
            weatherTextView.text = getCurrentWeather(weather.weather.first().main)["weather"]


            // 온도 및 시간 설정
            temperatureTextView.text = "${weather.main.getTempInCelsius()}°C"
            timeTextView.text = weather.dateTime.split(" ")[0].split("-")[1] + "." +
                    weather.dateTime.split(" ")[0].split("-")[2] + " " + weather.dateTime.split(" ")[1].split(":")[0] + "시"
        }
    }
}