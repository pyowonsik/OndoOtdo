package com.wspyo.ondootdo

import WeatherForecastResponse
import android.content.Context
import android.graphics.Typeface
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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.model.weather.WeatherResponse

import com.wspyo.ondootdo.viewModel.WeatherViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var weatherViewModel : WeatherViewModel

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
        val weatherForecastResponse : WeatherForecastResponse? = weatherViewModel.weatherForecast.value

        binding.TemperatureArea.text = weatherResponse?.main?.getTempInCelsius().toString() + "°C"
        binding.TitleTextArea.text = weatherViewModel.address.value.toString() + " 오늘의 날씨"

        val imageName = getCurrentWeather(weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())["weatherImg"]

        val resourceId = resources.getIdentifier(imageName, "drawable", requireContext().packageName)
        binding.WeatherImageArea.setImageResource(resourceId)
        binding.WeatherArea.text = getCurrentWeather(weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())["weather"]


        val precipitation =
            if(weatherResponse?.snow != null) weatherResponse?.snow?.oneHour
            else if(weatherResponse?.rain != null) weatherResponse.rain.oneHour
            else 0.0

        binding.Precipitation.text = "${precipitation}mm"
        if(weatherResponse?.snow != null) binding.PrecipitationImageArea.setImageResource(R.drawable.fall)
        if(weatherResponse?.snow != null) binding.PrecipitationTitleArea.text = "강우량(mm/hr)"

        binding.SunLight.text = weatherResponse?.sys?.getCurrentSunLight().toString() + "시간"

        // 텍스트뷰에 적용
        binding.RecommendWearInfoArea.text = getSpannableString(weatherResponse!!,requireContext())

        return binding.root
    }
}


fun getCurrentWeather(weather:String) : MutableMap<String,String> {

    return when (weather) {
        "Clear" -> mutableMapOf("weatherImg" to "clear", "weather" to "맑음")
        "Clouds" -> mutableMapOf("weatherImg" to "cloud", "weather" to "흐림")
        "Rain" -> mutableMapOf("weatherImg" to "rain", "weather" to "비")
        "Snow" -> mutableMapOf("weatherImg" to "snow", "weather" to "눈")
        "Drizzle" -> mutableMapOf("weatherImg" to "rain", "weather" to "이슬비")
        "Mist" -> mutableMapOf("weatherImg" to "strong_cloud", "weather" to "안개")
        "Smoke" -> mutableMapOf("weatherImg" to "cloud", "weather" to "연기")
        "Snow" -> mutableMapOf("weatherImg" to "cloud", "weather" to "실안개")
        "Dust" -> mutableMapOf("weatherImg" to "cloud", "weather" to "먼지")
        "Fog" -> mutableMapOf("weatherImg" to "strong_cloud", "weather" to "안개")
        "Sand" -> mutableMapOf("weatherImg" to "cloud", "weather" to "모래")
        "Ash" -> mutableMapOf("weatherImg" to "cloud", "weather" to "먼지")
        "Thunderstorm" -> mutableMapOf("weatherImg" to "strong_rain", "weather" to "천둥번개")
        "Squall" -> mutableMapOf("weatherImg" to "strong_rain", "weather" to "돌풍")
        "Tornado" -> mutableMapOf("weatherImg" to "strong_rain", "weather" to "토네이도")
        else -> mutableMapOf()
    }
}
fun getClothingRecommendation(temp: Int, weather: String): String {
    // 기본 옷 추천 문구
    val clothingRecommendation = when {
        temp >= 28 -> "오늘은 정말 더워요! 민소매나 반팔에 반바지로 가볍게 입어보세요. 원피스도 시원한 선택이에요!"
        temp in 23..27 -> "따뜻한 날씨에요! 반팔이나 얇은 셔츠를 추천드려요. 반바지나 면바지도 무난하겠어요."
        temp in 20..22 -> "조금 시원한 날씨네요. 얇은 가디건에 긴팔을 입고, 청바지나 면바지로 마무리해보세요."
        temp in 17..19 -> "오늘은 살짝 쌀쌀해요. 얇은 니트나 맨투맨에 가디건을 겹쳐 입고, 청바지가 좋을 거예요."
        temp in 12..16 -> "오늘은 자켓이나 가디건을 걸치기 좋은 날씨예요. 청바지나 면바지를 입고 따뜻하게 입으세요."
        temp in 9..11 -> "차가운 날씨입니다. 자켓이나 트렌치코트에 니트를 입고, 청바지로 따뜻하게 입으세요."
        temp in 5..8 -> "오늘은 코트나 가죽 자켓을 입는 게 좋겠어요. 히트텍과 니트를 레이어드하고, 레깅스를 입으면 따뜻할 거예요."
        else -> "오늘은 정말 추운 날씨네요. 패딩이나 두꺼운 코트에 목도리를 더해서 따뜻하게 입으세요!"
    }

    // 날씨에 따른 추가 문구
    val weatherAdvice = when (weather) {
        "Rain", "Drizzle" -> "비가 오니 우산이나 우비를 챙기세요."
        "Snow" -> "눈이 오니 핫팩을 챙기세요."
//            "Dust", "Haze" -> "황사가 심하니 마스크를 착용하세요."
        else -> ""
    }

    return "$clothingRecommendation $weatherAdvice".trim()
}


fun getSpannableString(weatherResponse: WeatherResponse , context : Context) : SpannableString {

    val recommendationText = getClothingRecommendation(weatherResponse?.main?.getTempInCelsius()!!,weatherResponse?.weather?.firstOrNull()?.getCurrentWeather().toString())

    // 옷에 해당하는 단어들
    val clothingItems = listOf(
        "민소매", "반팔", "반바지", "원피스",
        "얇은 셔츠", "면바지",
        "얇은 가디건", "긴팔", "청바지",
        "얇은 니트", "맨투맨", "가디건",
        "자켓", "트렌치코트", "니트",
        "코트", "가죽 자켓", "히트텍", "레깅스",
        "패딩", "두꺼운 코트", "목도리",
        "우산","우비","핫팩"
    )

    val spannableString = SpannableString(recommendationText)

    val color = ContextCompat.getColor(context, R.color.dark_orange)

    // 각 옷 아이템에 대해 텍스트에서 위치를 찾아 색상 적용
    clothingItems.forEach { item ->
        val startIndex = recommendationText.indexOf(item)
        if (startIndex >= 0) {
            // 색상을 적용할 범위를 설정
            val endIndex = startIndex + item.length

            // 특정 텍스트에 색상 적용
            spannableString.setSpan(
                ForegroundColorSpan(color),  // @color/dark_orange
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 폰트 굵기 (Bold) 적용
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),  // Bold로 스타일 적용
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return  spannableString
}