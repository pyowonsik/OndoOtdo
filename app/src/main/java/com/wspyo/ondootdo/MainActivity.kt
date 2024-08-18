package com.wspyo.ondootdo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.wspyo.ondootdo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView
    private lateinit var getLocationButton: Button
    private lateinit var getTemperatureButton: Button

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (fineLocationGranted || coarseLocationGranted) {
                checkLocationSettings()
            } else {
                locationTextView.text = "위치 권한이 필요합니다."
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationTextView = binding.locationTextView
        getLocationButton = binding.getLocationButton
        getTemperatureButton = binding.getTemperatureButton

//        getLocationButton.setOnClickListener {
            if (hasLocationPermissions()) {
                checkLocationSettings()
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
//        }

    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocationPermission && coarseLocationPermission
    }

    private fun checkLocationSettings() {
        val locationMode = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )

        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            locationTextView.text = "위치 서비스가 비활성화되어 있습니다. 위치 서비스를 활성화해주세요."
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (!hasLocationPermissions()) {
            locationTextView.text = "위치 권한이 없습니다."
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                val location = locationResult.lastLocation
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    locationTextView.text = "위도: $latitude, 경도: $longitude"

                    // Convert coordinates to address
                    convertCoordinatesToAddress(latitude, longitude)
                } else {
                    locationTextView.text = "위치를 가져올 수 없습니다."
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
            .addOnFailureListener { e ->
                locationTextView.text = "위치 요청 실패: ${e.message}"
            }
    }

    private fun convertCoordinatesToAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressText = buildString {
                    append("주소: ${address.getAddressLine(0)}\n")
//                    append("동: ${address.subLocality ?: "정보 없음"}\n")
//                    append("구: ${address.subAdminArea ?: "정보 없음"}\n")
//                    append("도시: ${address.locality ?: "정보 없음"}\n")
//                    append("국가: ${address.countryName ?: "정보 없음"}")
                }
                locationTextView.text = addressText
            } else {
                locationTextView.text = "주소를 가져올 수 없습니다."
            }
        } catch (e: Exception) {
            Log.e("GeocoderError", "주소 변환 실패: ${e.message}")
            locationTextView.text = "주소 변환 실패"
        }
    }
}
