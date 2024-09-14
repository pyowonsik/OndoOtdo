package com.wspyo.ondootdo

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.wspyo.ondootdo.viewModel.TimesViewModel
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {

            // Application을 통해 ViewModel에 접근
            weatherViewModel = (it.applicationContext as MyApplication).weatherViewModel

            var currentTemp = weatherViewModel.weatherResponse.value?.main?.getTempInCelsius().toString()
            var currentWeather = weatherViewModel.weatherResponse.value?.weather?.firstOrNull()?.getCurrentWeather()

            // Android 13 이상에서는 POST_NOTIFICATIONS 권한이 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 알림을 표시하지 않음
                return
            }

            // 알림 생성
            val notification = NotificationCompat.Builder(it, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("OndoOtdo 알림")
                .setStyle(NotificationCompat.BigTextStyle().bigText("현재 온도 : ${currentTemp}도\n현재 날씨 : ${currentWeather}"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            // 알림 표시
            with(NotificationManagerCompat.from(it)) {
                notify(1, notification)
            }
        }
    }
}