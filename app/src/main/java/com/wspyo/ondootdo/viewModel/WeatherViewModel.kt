package com.wspyo.ondootdo.viewModel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.wspyo.ondootdo.model.weather.WeatherResponse
import com.wspyo.ondootdo.repository.TemperatureRepository
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    // 위도,경도
    private var _latitude = MutableLiveData<Double>()
    val latitude : LiveData<Double>
        get() = _latitude


    private var _longitude = MutableLiveData<Double>()
    val longitude : LiveData<Double>
        get() = _longitude

    // 날씨
    private var _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse : LiveData<WeatherResponse>
        get() = _weatherResponse

    // 주소
    private var _address = MutableLiveData<String>()
    val address : LiveData<String>
        get() = _address


    private val temperatureRepository = TemperatureRepository()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun setFusedLocationClient(client: FusedLocationProviderClient) {
        fusedLocationClient = client
    }

    fun getCurrentLocation() {

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

                    _latitude.value = location.latitude
                    _longitude.value = location.longitude

                    convertCoordinatesToAddress(latitude.value!!, longitude.value!!)
                    getCurrentTemperature(latitude.value!!,longitude.value!!,"dd488c2e7a32df4bc1e362d36f4a53ad")
                } else {
//                    _locationData.value = "위치를 가져올 수 없습니다."
                    Toast.makeText(getApplication(),"위치를 가져올 수 없습니다.",Toast.LENGTH_SHORT).show()

                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication(),
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
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getApplication<Application>().mainLooper)
            .addOnFailureListener { e ->
                Toast.makeText(getApplication(),"위치 요청 실패: ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun convertCoordinatesToAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(getApplication())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
//                Log.d("WeatherViewModel","${addresses}")
                var splitAddress = addresses[0].getAddressLine(0).split(" ")
//                val addressText = "${splitAddress[1] + " " + splitAddress[2]}"
                val addressText = "${splitAddress[1]}"

                _address.value = addressText
            } else {
                Toast.makeText(getApplication(),"주소를 가져올 수 없습니다.",Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(getApplication(),"위치 서비스가 비활성화되어 있습니다. 위치 서비스를 활성화해주세요.",Toast.LENGTH_SHORT).show()

        }
    }

    fun getCurrentTemperature(lat : Double,lon : Double , apiId : String) = viewModelScope.launch{
        _weatherResponse.value = temperatureRepository.getCurrentTemperature(lat,lon,apiId)
    }
}