package com.wspyo.ondootdo

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.model.WeatherResponse
import com.wspyo.ondootdo.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var weatherViewModel : WeatherViewModel


    // 28 ~    : 민소매 , 반팔 , 반바지 , 원피스
    // 27 ~ 23 : 반팔 , 얇은 셔츠 , 반바지 , 면바지
    // 22 ~ 20 : 얇은 가디건 , 긴팔 , 면바지 , 청바지
    // 19 ~ 17 : 얇은 니트 , 맨투맨 , 가디건 , 청바지
    // 16 ~ 12 : 자켓 , 가디건 , 야상 , 스타킹 , 청바지 , 면바지
    // 11 ~ 9  : 자켓 , 트렌치코트 , 야상 , 니트 , 청바지 , 스타킹
    // 8 ~ 5   : 코트 , 가죽자켓 , 히트텍 , 니트 , 레깅스
    // 4 ~     : 패딩 , 두꺼운코트 , 목도리 , 기모제품


    // 비 - 우산 , 우비
    // 눈 - 핫팩
    // 황사 - 마스크

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        weatherViewModel = (requireActivity().application as MyApplication).weatherViewModel
        val weatherResponse : WeatherResponse? = weatherViewModel.weatherResponse.value
//        Log.d("ViewModel Test : MainFragment",weatherViewModel.weatherResponse.value.toString())

        binding.TemperatureTextView.text = weatherResponse?.main?.getTempInCelsius().toString()
        binding.locationTextView.text = weatherViewModel.address.value.toString()
        binding.WeatherTextView.text = weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString()

//        binding.timeFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
//        }
//
//        binding.searchFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
//        }


        // 오늘 날짜 가져오기
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        binding.dateTextView.text = formattedDate


        getOutfitByTemperature(weatherResponse?.main?.getTempInCelsius()!!)

        return binding.root
    }


    private fun getOutfitByTemperature(temperature: Double) {
        val outfit: List<String> = when {
            temperature >= 28.0 -> listOf("민소매", "반팔", "반바지", "원피스")
            temperature in 23.0..27.9 -> listOf("반팔", "얇은 셔츠", "반바지", "면바지")
            temperature in 20.0..22.9 -> listOf("얇은 가디건", "긴팔", "면바지", "청바지")
            temperature in 17.0..19.9 -> listOf("얇은 니트", "맨투맨", "가디건", "청바지")
            temperature in 12.0..16.9 -> listOf("자켓", "가디건", "야상", "스타킹", "청바지", "면바지")
            temperature in 9.0..11.9 -> listOf("자켓", "트렌치코트", "야상", "니트", "청바지", "스타킹")
            temperature in 5.0..8.9 -> listOf("코트", "가죽자켓", "내복", "니트", "레깅스")
            else -> listOf("패딩", "두꺼운코트", "목도리", "기모제품")
        }
        binding.WearTextView.text = outfit.joinToString(", ")
    }

}