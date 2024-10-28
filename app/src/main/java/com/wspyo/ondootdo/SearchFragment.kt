package com.wspyo.ondootdo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wspyo.ondootdo.adapter.PlaceRVAdapter
import com.wspyo.ondootdo.databinding.FragmentSearchBinding
import com.wspyo.ondootdo.repository.TemperatureRepository
import com.wspyo.ondootdo.viewModel.LocalViewModel
import com.wspyo.ondootdo.viewModel.PlaceDetailsViewModel
import com.wspyo.ondootdo.viewModel.TimesViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var localViewModel: LocalViewModel
    private lateinit var placeRVAdapter: PlaceRVAdapter
    private val placeDetailViewModel : PlaceDetailsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false)

        localViewModel = ViewModelProvider(this).get(LocalViewModel::class.java)

        val rv = binding.rv

        val temperatureRepository = TemperatureRepository()


        binding.searchIcon.setOnClickListener{
//            Log.d("SearchFragment",binding.searchEditTextArea.text.toString())
//            지역 검색 -> 검색어를 이용해 좌표값 -> 좌표 값을 이용해 weatherApi 호출
//            Toast.makeText(requireContext(),binding.searchEditTextArea.text.toString(),Toast.LENGTH_SHORT).show()
            var searchPlace = binding.searchEditTextArea.text.toString()
            localViewModel.getPlaceResponse(searchPlace)
            // weatherResponse 관찰자는 한 번만 등록되도록 변경

            placeDetailViewModel.weatherResponse.observe(viewLifecycleOwner) { temperature ->
                val dialogFragment = PlaceDetailsFragment.newInstance(temperature)
                dialogFragment.show(parentFragmentManager, "placeDetail")
            }

            localViewModel.placeResponse.observe(viewLifecycleOwner) {
                placeRVAdapter = PlaceRVAdapter(it.documents)
                rv.adapter = placeRVAdapter
                rv.layoutManager = LinearLayoutManager(requireContext())

                placeRVAdapter.itemClick = object : PlaceRVAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        // 클릭 시 데이터를 요청하지만 observe는 다시 설정하지 않음
                        placeDetailViewModel.getPlaceDetailWeather(
                            it.documents[position].y.toDouble(),
                            it.documents[position].x.toDouble(),
                            "dd488c2e7a32df4bc1e362d36f4a53ad"
                        )
                    }
                }
            }

        }
        return binding.root
    }
}