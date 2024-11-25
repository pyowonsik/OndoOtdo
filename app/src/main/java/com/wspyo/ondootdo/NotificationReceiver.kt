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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let {

            // Application을 통해 ViewModel에 접근
            weatherViewModel = (it.applicationContext as MyApplication).weatherViewModel

            val currentTemp = weatherViewModel.weatherResponse.value?.main?.getTempInCelsius()?.toString() ?: "정보 없음"
            val currentWeather = weatherViewModel.weatherResponse.value?.weather?.firstOrNull()?.getCurrentWeather() ?: "정보 없음"

            // Android 13 이상에서는 POST_NOTIFICATIONS 권한이 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 알림을 표시하지 않음
                return
            }

            // 알림 채널 생성
            createNotificationChannel(it)

            // 알림 생성
            val notification = NotificationCompat.Builder(it, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("OndoOtdo 알림")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("현재 온도: ${currentTemp}도\n현재 날씨: ${currentWeather}")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            // 알림 표시
            with(NotificationManagerCompat.from(it)) {
                notify(1, notification)
            }

            // 진동 추가 (다섯 번 반복)
            vibratePhone(it)
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

    // 다섯 번 반복 진동 메서드
    private fun vibratePhone(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationDuration = 500L // 진동 시간 (밀리초)
        val pauseDuration = 300L // 대기 시간 (밀리초)
        val repeatCount = 20 // 반복 횟수

        // 진동 패턴 생성
        val pattern = mutableListOf<Long>().apply {
            add(0) // 처음 시작 대기 시간
            repeat(repeatCount) {
                add(vibrationDuration) // 진동 시간
                add(pauseDuration) // 대기 시간
            }
        }.toLongArray()

        // 진동 패턴을 5번 반복
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0)) // 0으로 설정하여 계속 반복
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0) // 0으로 설정하여 계속 반복
        }
    }
}
