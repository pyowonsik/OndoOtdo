import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("list") val forecasts: List<WeatherForecast>
)

data class WeatherForecast(
    @SerializedName("main") val main: Main,
    @SerializedName("dt_txt") val dateTime: String
)

data class Main(
    @SerializedName("temp") val temperature: Double
)
