package com.wspyo.ondootdo



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wspyo.ondootdo.adapter.PlaceRVAdapter
import com.wspyo.ondootdo.databinding.FragmentSearchBinding
import com.wspyo.ondootdo.viewModel.LocalViewModel
import com.wspyo.ondootdo.viewModel.PlaceDetailsViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val localViewModel: LocalViewModel by activityViewModels ()
    private lateinit var placeRVAdapter: PlaceRVAdapter
    private val placeDetailViewModel : PlaceDetailsViewModel by activityViewModels()
    private var isDialogOpen : Boolean = false
    private lateinit var placeName : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false)

        val rv = binding.rv

        // RecyclerView 초기화
        placeRVAdapter = PlaceRVAdapter(listOf())
        rv.adapter = placeRVAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        if(localViewModel.placeResponse.value == null ){
            binding.rv.visibility = View.GONE
            binding.EmptyDataArea.visibility = View.VISIBLE
        }

        // placeResponse 관찰자도 한 번만 설정
        localViewModel.placeResponse.observe(viewLifecycleOwner) { placeResponse ->
            placeRVAdapter.updateData(placeResponse.documents)


            if(placeResponse != null){
                binding.rv.visibility = View.VISIBLE
                binding.EmptyDataArea.visibility = View.GONE
            }

            if(placeResponse.documents.isEmpty()){
                binding.rv.visibility = View.GONE
                binding.EmptyDataArea.visibility = View.VISIBLE
            }

            // PlaceRVAdapter의 아이템 클릭 리스너 설정
            placeRVAdapter.itemClick = object : PlaceRVAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {

                    // 클릭 시 데이터 요청 (observe는 다시 설정하지 않음)
                    // 클릭시 placeDetailViewModel의 weatherResponse 데이터 변경
                    // -> 변경이 된 데이터를 감지하여 해당 데이터로 place detail Dialog open
                    val document = placeResponse.documents[position]
//                    Log.d("SearchFragment",  document.toString())

                    isDialogOpen = true

                    placeName = document.place_name

                    placeDetailViewModel.getPlaceDetailWeather(
                        document.y.toDouble(),
                        document.x.toDouble(),
                        "dd488c2e7a32df4bc1e362d36f4a53ad"
                    )
                }
            }
        }

        // weatherResponse 관찰자는 최초에 한 번만 등록
        placeDetailViewModel.weatherResponse.observe(viewLifecycleOwner) { temperature ->
            if(isDialogOpen){
                val dialogFragment = PlaceDetailsFragment.newInstance(temperature,placeName)
                dialogFragment.show(parentFragmentManager, "placeDetail")
            }
        }

        // 검색 버튼 클릭 시에만 데이터 요청
        binding.searchIcon.setOnClickListener {

            if(binding.searchEditTextArea.text.toString() == "") {
                Toast.makeText(requireContext(),"주소를 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else{
                val searchPlace = binding.searchEditTextArea.text.toString()
                localViewModel.getPlaceResponse(searchPlace)
            }

        }

        return binding.root
    }
}