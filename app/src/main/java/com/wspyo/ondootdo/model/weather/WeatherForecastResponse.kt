import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class WeatherForecastResponse(
    @SerializedName("list") val forecasts: List<WeatherForecast>
)

data class WeatherForecast(
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather : List<Weather>,
    @SerializedName("dt_txt") val dateTime: String
)

data class Weather (
    @SerializedName("main") val main : String
)

data class Main(
    @SerializedName("temp") val temperature: Double
){
    private fun roundToFirstDecimal(value: Double): Double {
        return (value * 10.0).roundToInt() / 10.0
    }

    fun getTempInCelsius(): Int {
        return Math.round(roundToFirstDecimal(temperature - 273.15)).toInt()
    }
}
