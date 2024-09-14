package com.wspyo.ondootdo
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wspyo.ondootdo.databinding.ActivityMainBinding
import com.wspyo.ondootdo.entity.TimeEntity
import com.wspyo.ondootdo.viewModel.TimesViewModel
import com.wspyo.ondootdo.viewModel.WeatherViewModel
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var timesViewModel: TimesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        timesViewModel = ViewModelProvider(this).get(TimesViewModel::class.java)



        // notification channel 생성
        createNotificationChannel()

        // Android 12 이상에서 SCHEDULE_EXACT_ALARM 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isExactAlarmPermissionGranted()) {
            Toast.makeText(this, "Please enable 'Set exact alarms' permission in settings.", Toast.LENGTH_LONG).show()
            // 사용자를 설정 화면으로 안내하여 권한을 요청
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        } else {

            timesViewModel.getAllTimes()
            timesViewModel.times.observe(this) {
                Log.d("MainActivity", it.map { time -> time.time }.toString())
            }

            // 특정 시간 저장
            val calendar = Calendar.getInstance().apply {
//                add(Calendar.SECOND, 10) // 10초 후로 설정
                set(Calendar.HOUR_OF_DAY, 15) // 오후 3시
                set(Calendar.MINUTE, 35)      // 30분
                set(Calendar.SECOND, 0)       // 0초
            }

            // 현재 시간보다 이전이면 하루 뒤로 조정
            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            // 저장된 시간 알림 실행
            scheduleNotification(calendar.timeInMillis)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "LocalNotificationChannel"
            val descriptionText = "This is a channel for local notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(timeInMillis: Long) {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

    // Android 12 이상에서 정확한 알람 권한이 부여되었는지 확인
    private fun isExactAlarmPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}
