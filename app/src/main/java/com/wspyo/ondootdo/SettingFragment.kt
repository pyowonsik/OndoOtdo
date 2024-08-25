package com.wspyo.ondootdo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wspyo.ondootdo.adapter.TimeRVAdapter
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var rvAdapter: TimeRVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);

        val rv = binding.timeRv
        val items = mutableListOf<String>("11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00")

        rvAdapter = TimeRVAdapter(items, requireContext())
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        binding.mainFragmentTab.setOnClickListener(){
            it.findNavController().navigate(R.id.action_settingFragment_to_mainFragment)
        }

        return binding.root
    }
}