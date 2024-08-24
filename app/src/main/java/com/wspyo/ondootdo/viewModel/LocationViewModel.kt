package com.wspyo.ondootdo.viewModel
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private var _latitude = MutableLiveData<Double>()
    val latitude : LiveData<Double>
        get() = _latitude


    private var _longitude = MutableLiveData<Double>()
    val longitude : LiveData<Double>
        get() = _longitude


    private val _locationData = MutableLiveData<String>()
    val locationData: LiveData<String> = _locationData

    private val _addressData = MutableLiveData<String>()
    val addressData: LiveData<String> = _addressData

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
                    val latitude = location.latitude
                    val longitude = location.longitude

                    _latitude.value = latitude
                    _longitude.value = longitude

                    _locationData.value = "위도: $latitude, 경도: $longitude"
                    convertCoordinatesToAddress(latitude, longitude)
                } else {
                    _locationData.value = "위치를 가져올 수 없습니다."
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
                _locationData.value = "위치 요청 실패: ${e.message}"
            }
    }

    private fun convertCoordinatesToAddress(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(getApplication())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val addressText = "주소: ${address.getAddressLine(0)}"
                _addressData.value = addressText
            } else {
                _addressData.value = "주소를 가져올 수 없습니다."
            }
        } catch (e: Exception) {
            _addressData.value = "주소 변환 실패: ${e.message}"
        }
    }
}
