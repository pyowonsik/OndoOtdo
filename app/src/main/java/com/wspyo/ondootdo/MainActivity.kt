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
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        timesViewModel = ViewModelProvider(this).get(TimesViewModel::class.java)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // 초기 Fragment 설정 및  BottomNavigationView 클릭 리스너 설정
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MainFragment())
                        .commit()
                    true
                }
                R.id.nav_settings-> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingFragment())
                        .commit()
                    true
                }
                R.id.nav_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        //

        // notification channel 생성
        createNotificationChannel()

        // Android 12 이상에서 SCHEDULE_EXACT_ALARM 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isExactAlarmPermissionGranted()) {
            Toast.makeText(this, "Please enable 'Set exact alarms' permission in settings.", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        } else {
            timesViewModel.getAllTimes()

            // ViewModel에서 시간을 가져와 알림을 예약
            timesViewModel.times.observe(this) { timesList ->
                for (timeData in timesList) {
                    val timeString = timeData.time // "18:24", "06:24" 등의 형식

                    // String을 ":" 기준으로 분리하여 hour와 minute 추출
                    val timeParts = timeString.split(":")
                    val hour = timeParts[0].toInt()
                    val minute = timeParts[1].toInt()

                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }

                    // 현재 시간보다 이전이면 하루 뒤로 조정
                    if (calendar.timeInMillis <= System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                    }

                    // 알람 예약 또는 취소 (isEnabled에 따라 처리)
                    scheduleNotification(calendar.timeInMillis, timeData.isEnabled, timeData.id)
                }

            }
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
    private fun scheduleNotification(timeInMillis: Long, isEnabled: Boolean, requestCode: Int) {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode, // 각 알람을 구분할 ID
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (isEnabled) {
            // 알람 예약
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        } else {
            // isEnabled가 false일 경우 알람 취소
            alarmManager.cancel(pendingIntent)
        }
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
