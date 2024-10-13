package com.wspyo.ondootdo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.wspyo.ondootdo.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container,false)

        binding.SearchBtn.setOnClickListener{
//            Log.d("SearchFragment",binding.searchEditTextArea.text.toString())
//            지역 검색 -> 검색어를 이용해 좌표값 -> 좌표 값을 이용해 weatherApi 호출
//            Toast.makeText(requireContext(),binding.searchEditTextArea.text.toString(),Toast.LENGTH_SHORT).show()
            var searchAddress = binding.searchEditTextArea.text

        }


        return binding.root
    }
}