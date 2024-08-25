package com.wspyo.ondootdo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (fineLocationGranted || coarseLocationGranted) {
                checkLocationSettings()
            } else {
                Toast.makeText(this,"위치 권한이 필요합니다.",Toast.LENGTH_SHORT).show()
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel = (application as MyApplication).weatherViewModel


        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        weatherViewModel.setFusedLocationClient(fusedLocationClient)

        if (hasLocationPermissions()) {
            checkLocationSettings()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }

        weatherViewModel.weatherResponse.observe(this){
//            Log.d("SplashActivity",it.main.getTempInCelsius().toString())
            if(it.main.getTempInCelsius() != null){
//                Handler().postDelayed({
//                Log.d("ViewModel Test : SplashActivity",weatherViewModel.weatherResponse.value.toString())
                startActivity(Intent(this,MainActivity::class.java))
                finish()
//                },3000)
            }
        }
    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocationPermission && coarseLocationPermission
    }

    private fun checkLocationSettings() {
        val locationMode = Settings.Secure.getInt(
            this.contentResolver,
            Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )

        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            Toast.makeText(this,"위치 서비스가 비활성화되어 있습니다. 위치 서비스를 활성화해주세요.",Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            weatherViewModel.getCurrentLocation()
        }
    }
}