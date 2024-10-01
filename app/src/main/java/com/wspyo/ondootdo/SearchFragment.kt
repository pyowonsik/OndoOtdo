package com.wspyo.ondootdo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//        binding.mainFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_searchFragment_to_mainFragment)
//        }
//
//        binding.timeFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_searchFragment_to_settingFragment)
//        }

        return binding.root
    }
}