package com.wspyo.ondootdo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var weatherViewModel : WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        weatherViewModel = (requireActivity().application as MyApplication).weatherViewModel

//        Log.d("ViewModel Test : MainFragment",weatherViewModel.weatherResponse.value.toString())

        binding.TemperatureTextView.text = weatherViewModel.weatherResponse.value?.main?.getTempInCelsius().toString()
        binding.locationTextView.text = weatherViewModel.address.value.toString()

        binding.timeFragmentTab.setOnClickListener(){
            it.findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
        }

        return binding.root
    }
}