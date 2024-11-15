package com.wspyo.ondootdo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wspyo.ondootdo.adapter.PlaceRVAdapter
import com.wspyo.ondootdo.databinding.FragmentSearchBinding
import com.wspyo.ondootdo.viewModel.LocalViewModel
import com.wspyo.ondootdo.viewModel.PlaceDetailsViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val localViewModel: LocalViewModel by activityViewModels()
    private lateinit var placeRVAdapter: PlaceRVAdapter
    private val placeDetailViewModel: PlaceDetailsViewModel by activityViewModels()
    private var isDialogOpen: Boolean = false
    private lateinit var placeName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val rv = binding.rv

        // RecyclerView 초기화
        placeRVAdapter = PlaceRVAdapter(listOf())
        rv.adapter = placeRVAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        // 무한 스크롤 리스너 추가
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                // 마지막 아이템이 화면에 보일 때 무한 스크롤 처리
                if (!localViewModel.isEnd && lastVisibleItem + 1 >= totalItemCount) {
                    val searchPlace = binding.searchEditTextArea.text.toString()
                    if (searchPlace.isNotBlank()) {
                        localViewModel.getPlaceResponse(searchPlace)
                    }
                }
            }
        })

        if (localViewModel.placeResponse.value == null) {
            binding.rv.visibility = View.GONE
            binding.EmptyDataArea.visibility = View.VISIBLE
        }

        // placeResponse 관찰자 설정
        localViewModel.placeResponse.observe(viewLifecycleOwner) { placeResponse ->
            placeRVAdapter.updateData(placeResponse)

            if (placeResponse != null) {
                binding.rv.visibility = View.VISIBLE
                binding.EmptyDataArea.visibility = View.GONE
            }

            if (placeResponse.isEmpty()) {
                binding.rv.visibility = View.GONE
                binding.EmptyDataArea.visibility = View.VISIBLE
            }

            // PlaceRVAdapter의 아이템 클릭 리스너 설정
            placeRVAdapter.itemClick = object : PlaceRVAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    if (isDialogOpen) return

                    isDialogOpen = true
                    val document = placeResponse[position]
                    placeName = document.place_name
                    placeDetailViewModel.getPlaceDetailWeather(
                        document.y.toDouble(),
                        document.x.toDouble(),
                        "dd488c2e7a32df4bc1e362d36f4a53ad"
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        isDialogOpen = false
                    }, 500)
                }
            }

            // weatherResponse 관찰자 설정
            placeDetailViewModel.weatherResponse.observe(viewLifecycleOwner) { temperature ->
                if (isDialogOpen) {
                    val dialogFragment = PlaceDetailsFragment.newInstance(temperature, placeName)
                    dialogFragment.show(parentFragmentManager, "placeDetail")
                }
            }
        }

        // 검색 버튼 클릭 시 초기화 및 데이터 요청
        binding.searchIcon.setOnClickListener {
            val searchPlace = binding.searchEditTextArea.text.toString()
            if (searchPlace.isBlank()) {
                Toast.makeText(requireContext(), "주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                localViewModel.resetSearch() // 검색 초기화
                localViewModel.getPlaceResponse(searchPlace)
            }
        }

        return binding.root
    }
}
