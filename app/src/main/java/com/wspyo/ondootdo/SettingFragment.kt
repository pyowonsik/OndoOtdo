package com.wspyo.ondootdo

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
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
    private val timesViewModel: TimesViewModel by activityViewModels()

    var lastClickTime = 0L

    fun isDoubleClickPrevented(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < 500) { // 500ms 이내 클릭 무시
            return true
        }
        lastClickTime = currentTime
        return false
    }

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


        if(timesViewModel.times.value == null || timesViewModel.times.value!!.isEmpty() ){
            binding.rv.visibility = View.GONE
            binding.EmptyDataArea.visibility = View.VISIBLE
        }
        timesViewModel.times.observe(viewLifecycleOwner, Observer {
            rvAdapter = TimeRVAdapter(it as MutableList<TimeEntity>, requireContext())
            rv.adapter = rvAdapter
            rv.layoutManager = LinearLayoutManager(requireContext())


            if(timesViewModel.times.value == null || timesViewModel.times.value!!.isEmpty() ){
                binding.rv.visibility = View.GONE
                binding.EmptyDataArea.visibility = View.VISIBLE
            }else{
                binding.rv.visibility = View.VISIBLE
                binding.EmptyDataArea.visibility = View.GONE
            }

            rvAdapter.itemClick = object : TimeRVAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    if (isDoubleClickPrevented()) return // 중복 클릭 방지

                    val dialogFragment = TimeDetailsFragment.newInstance(it[position].id, false)
                    dialogFragment.show(parentFragmentManager, "timesDetail")
                }

                override fun alarmClick(view: View, position: Int) {
                    if (isDoubleClickPrevented()) return // 중복 클릭 방지

                    timesViewModel.updateAlarmStatus(it[position].id, !it[position].isEnabled)
                }
            }

            Log.d("SettingFragment", it.toString())
        })
        //


        binding.timeAddBtn.setOnClickListener() {
            val dialogFragment = TimeDetailsFragment.newInstance(0, true)
            dialogFragment.show(parentFragmentManager, "customTimePicker")
        }

        binding.DeleteAllBtn.setOnClickListener() {
            timesViewModel.deleteAllTimes()
        }


        return binding.root
    }
}
