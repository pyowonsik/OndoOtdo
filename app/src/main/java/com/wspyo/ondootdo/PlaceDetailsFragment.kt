package com.wspyo.ondootdo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.wspyo.ondootdo.databinding.FragmentPlaceDetailsBinding
import com.wspyo.ondootdo.model.weather.WeatherResponse
import java.io.Serializable


class PlaceDetailsFragment : DialogFragment() {

    private  lateinit var  binding : FragmentPlaceDetailsBinding

    companion object {
        fun newInstance(weatherResponse : WeatherResponse): PlaceDetailsFragment {
            val fragment = PlaceDetailsFragment()

            val args = Bundle()
            args.putSerializable("weatherResponse", weatherResponse)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_details, null, false)

        val weatherResponse : WeatherResponse = arguments?.getSerializable("weatherResponse") as WeatherResponse



        binding.TitleTextArea.text = ""

        val imageName = getCurrentWeather(weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())["weatherImg"]
        val resourceId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)
        binding.WeatherImageArea.setImageResource(resourceId)

        binding.TemperatureTextArea.text = "기온 : " + weatherResponse?.main?.getTempInCelsius().toString() + "°C"
        binding.WeatherTextArea.text = "날씨 : " + getCurrentWeather(weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())["weather"]

        binding.TemperatureTextArea3.text = ""
        binding.Description.text = getClothingRecommendation(weatherResponse?.main?.getTempInCelsius()!!,weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())


        // 텍스트뷰에 적용
        binding.Description.text = getSpannableString(weatherResponse,requireContext())


        binding.CloseBtn.setOnClickListener(){
            dismiss()
        }


        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 설정
        return dialog
    }

}
