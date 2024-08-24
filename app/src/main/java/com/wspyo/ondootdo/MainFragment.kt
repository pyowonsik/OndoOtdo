package com.wspyo.ondootdo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.wspyo.ondootdo.databinding.ActivityMainBinding
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.viewModel.LocationViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private  val locationViewModel : LocationViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (fineLocationGranted || coarseLocationGranted) {
                checkLocationSettings()
            } else {
                binding.locationTextView.text = "위치 권한이 필요합니다."
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);


        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationViewModel.setFusedLocationClient(fusedLocationClient)

        locationViewModel.locationData.observe(requireActivity(), Observer { locationText ->
            binding.locationTextView.text = locationText
        })

        locationViewModel.addressData.observe(requireActivity(), Observer { addressText ->
            binding.locationTextView.text = addressText
        })

        binding.getLocationButton.setOnClickListener {
            if (hasLocationPermissions()) {
                checkLocationSettings()
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }

        binding.getTemperatureButton.setOnClickListener{
            val latitude = locationViewModel.latitude
            val longitude = locationViewModel.longitude

            Log.d("MainActivity",latitude.value.toString())
            Log.d("MainActivity",longitude.value.toString())
        }


        return binding.root
    }

    private fun hasLocationPermissions(): Boolean {
        val fineLocationPermission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocationPermission && coarseLocationPermission
    }

    private fun checkLocationSettings() {
        val locationMode = Settings.Secure.getInt(
            requireActivity().contentResolver,
            Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )

        if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
            binding.locationTextView.text = "위치 서비스가 비활성화되어 있습니다. 위치 서비스를 활성화해주세요."
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else {
            locationViewModel.getCurrentLocation()
        }
    }
}