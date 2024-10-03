package com.wspyo.ondootdo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wspyo.ondootdo.adapter.WearRVAdapter
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.model.Wear
import com.wspyo.ondootdo.model.WeatherResponse

import com.wspyo.ondootdo.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var weatherViewModel : WeatherViewModel
    private lateinit var wearsList : MutableList<Wear>
    private lateinit var rvAdapter: WearRVAdapter



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

        binding.TemperatureTextView.text = weatherResponse?.main?.getTempInCelsius().toString() + "°"
        binding.locationTextView.text = weatherViewModel.address.value.toString()
        binding.WeatherTextView.text = weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString()


        // 오늘 날짜 가져오기
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        binding.dateTextView.text = formattedDate

        wearsList = getOutfitByTemperature(weatherResponse?.main?.getTempInCelsius()!!)

        val wearRv = binding.wearRv

        rvAdapter = WearRVAdapter(wearsList)
        wearRv.adapter = rvAdapter
        wearRv.layoutManager =  GridLayoutManager(requireContext(),2)

//        getOutfitByTemperature(weatherResponse?.main?.getTempInCelsius()!!)
        binding.description1.text = "오늘 날씨는 21도입니다.\n날씨가 상대적으로 시원하니 얇은 가디건 하나 챙기시는 걸 추천드려요.\n긴팔과 청바지도 적당한 선택일 거예요!"

        return binding.root
    }


    private fun getDrawableIdByName(context: Context, name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }
    private fun getOutfitByTemperature(temperature: Int) : MutableList<Wear> {


        val wear1 = mutableListOf<Wear>(
            Wear(getDrawableIdByName(requireContext(), "sleeveless_shirt"), "민소매"),
            Wear(getDrawableIdByName(requireContext(), "short_sleeve_shirt"), "반팔"),
            Wear(getDrawableIdByName(requireContext(), "short_pants"), "반바지"),
            Wear(getDrawableIdByName(requireContext(), "one_piece"), "원피스"),
        )

        val wear2 = mutableListOf<Wear>(
//            Wear(getDrawableIdByName(requireContext(), ""), "얇은 셔츠"),
            Wear(getDrawableIdByName(requireContext(), "short_sleeve_shirt"), "반팔"),
            Wear(getDrawableIdByName(requireContext(), "short_pants"), "반바지"),
            Wear(getDrawableIdByName(requireContext(), "one_piece"), "원피스"),
        )

        val wear3 = mutableListOf<Wear>(
            Wear(getDrawableIdByName(requireContext(), "thin_cardigan"), "얇은 가디건"),
            Wear(getDrawableIdByName(requireContext(), "long_sleeve_shirt"), "긴팔"),
            Wear(getDrawableIdByName(requireContext(), "noodle_pants"), "청바지"),
            Wear(getDrawableIdByName(requireContext(), "denim_pants"), "면바지"),
        )

        val outfit: MutableList<Wear> = when {
            temperature >= 28 -> wear1
            temperature in 23..27 -> wear2
            temperature in 20..22 -> wear3
//            temperature in 17..19 -> listOf("얇은 니트", "맨투맨", "가디건", "청바지")
//            temperature in 12..16 -> listOf("자켓", "가디건", "야상", "스타킹", "청바지", "면바지")
//            temperature in 9..11 -> listOf("자켓", "트렌치코트", "야상", "니트", "청바지", "스타킹")
//            temperature in 5..8 -> listOf("코트", "가죽자켓", "내복", "니트", "레깅스")
            else -> mutableListOf<Wear>()
        }
        return outfit
    }


//    fun getClothingRecommendation(temp: Int): String {
//        return when {
//            temp >= 25 -> "오늘 기온은 ${temp}도예요. 무더운 날씨엔 가벼운 반팔이나 반바지가 최고죠! 시원하게 입으시고 물도 많이 드세요!"
//            temp in 20..24 -> "오늘 날씨는 ${temp}도입니다. 날씨가 상대적으로 시원하니 얇은 가디건 하나 챙기시는 걸 추천드려요. 긴팔과 청바지도 적당한 선택일 거예요!"
//            temp in 15..19 -> "${temp}도네요! 서늘한 바람이 부니 긴팔과 면바지가 좋겠어요. 혹시 추울 수도 있으니, 얇은 자켓 하나 챙기면 더 든든하겠죠?"
//            temp in 10..14 -> "오늘은 ${temp}도입니다. 쌀쌀한 날씨니 따뜻한 니트나 두꺼운 외투를 입으시는 게 좋아요. 두꺼운 바지도 잊지 마세요!"
//            temp < 10 -> "와! 오늘은 ${temp}도라서 많이 추울 거예요. 패딩과 따뜻한 모자를 꼭 챙기세요! 장갑도 필수입니다!"
//            else -> "오늘은 기온이 특별하네요! 기분에 따라 편안한 옷을 선택해 보세요!"
//        }
//    }


}