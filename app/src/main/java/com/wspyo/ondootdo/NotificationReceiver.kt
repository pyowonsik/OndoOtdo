package com.wspyo.ondootdo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {

            // Application을 통해 ViewModel에 접근
            weatherViewModel = (it.applicationContext as MyApplication).weatherViewModel
            val currentTemp = weatherViewModel.weatherResponse.value?.main?.getTempInCelsius()?.toString()
            val currentWeather = weatherViewModel.weatherResponse.value?.weather?.firstOrNull()?.getCurrentWeather()

            Log.d("NotificationReceiver", currentTemp.toString())
            Log.d("NotificationReceiver", currentWeather.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 알림을 표시하지 않음
                return
            }

            val notificationMessage = if (!currentTemp.isNullOrEmpty() && !currentWeather.isNullOrEmpty()) {
                "현재 온도: ${currentTemp}도\n현재 날씨: ${currentWeather}"
            } else {
                "어플을 확인하여 오늘 기온 및 옷차림을 확인해보세요."
            }

            createNotificationChannel(it)

            val notification = NotificationCompat.Builder(it, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("OndoOtdo 알림")
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            with(NotificationManagerCompat.from(it)) {
                notify(1, notification)
            }

        }
    }

    // 알림 채널 생성 메서드
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", // NotificationCompat.Builder와 동일한 ID 사용
                "Default Channel", // 채널 이름
                NotificationManager.IMPORTANCE_HIGH // 중요도
            ).apply {
                description = "Channel for Ondootdo notifications"
                enableVibration(true) // 진동 활성화
                vibrationPattern = longArrayOf(0, 500, 100, 500) // 패턴 예시
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
