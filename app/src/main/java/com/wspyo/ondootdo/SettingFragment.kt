package com.wspyo.ondootdo

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wspyo.ondootdo.adapter.TimeRVAdapter
import com.wspyo.ondootdo.databinding.FragmentMainBinding
import com.wspyo.ondootdo.databinding.FragmentSettingBinding
import com.wspyo.ondootdo.entity.TimeEntity
import com.wspyo.ondootdo.viewModel.TimesViewModel
import java.util.Calendar

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var rvAdapter: TimeRVAdapter
    private val timesViewModel : TimesViewModel by activityViewModels()
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

        // Fragment에서 데이터 observe 할때는 , viewLifecycleOwner 사용
        timesViewModel.getAllTimes()
        timesViewModel.times.observe(viewLifecycleOwner, Observer {
            rvAdapter = TimeRVAdapter(it as MutableList<TimeEntity>, requireContext())
            rv.adapter = rvAdapter
            rv.layoutManager = LinearLayoutManager(requireContext())

            rvAdapter.itemClick = object : TimeRVAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    timesViewModel.updateAlarmStatus(it[position].id,!it[position].isEnabled)
                }
            }
        })
        //

        binding.timeAddBtn.setOnClickListener(){
            showTimePickerDialog()
        }

        binding.deleteAllBtn.setOnClickListener{
            timesViewModel.deleteAllTimes()
        }

//        binding.mainFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_settingFragment_to_mainFragment)
//        }
//
//        binding.searchFragmentTab.setOnClickListener(){
//            it.findNavController().navigate(R.id.action_settingFragment_to_searchFragment)
//        }



        return binding.root
    }
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.CustomTimePickerDialog, // 스타일 적용
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timesViewModel.insertTime(selectedTime)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

}