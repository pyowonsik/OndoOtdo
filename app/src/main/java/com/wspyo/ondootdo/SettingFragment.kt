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
                    val dialogFragment = CustomTimePickerDialogFragment.newInstance(it[position].id, false)
                    dialogFragment.show(parentFragmentManager, "customTimePicker")
                }
                override fun alarmClick(view : View , position : Int) {
                    timesViewModel.updateAlarmStatus(it[position].id,!it[position].isEnabled)
                }
            }
            Log.d("SettingFragment",it.toString())
        })
        //

        binding.timeAddBtn.setOnClickListener(){
            val dialogFragment = CustomTimePickerDialogFragment.newInstance(0, true)
            dialogFragment.show(parentFragmentManager, "customTimePicker")
        }

        binding.DeleteAllBtn.setOnClickListener(){
            timesViewModel.deleteAllTimes()
        }


        return binding.root
    }

    class CustomTimePickerDialogFragment() : DialogFragment() {

        private val timesViewModel : TimesViewModel by activityViewModels()

        // newInstance 메서드: 파라미터를 넘길 때 사용
        companion object {
            fun newInstance(id: Int, isInsert: Boolean): CustomTimePickerDialogFragment {
                val fragment = CustomTimePickerDialogFragment()

                val args = Bundle()
                args.putInt("id", id)
                args.putBoolean("isInsert", isInsert)

                fragment.arguments = args
                return fragment
            }
        }
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.custom_time_picker_dialog, null)

            // TimePicker 참조
            val timePicker = view.findViewById<TimePicker>(R.id.custom_time_picker)
            timePicker.setIs24HourView(true)

            val id = arguments?.getInt("id", -1) ?: -1
            val isInsert = arguments?.getBoolean("isInsert", true) ?: true

            val deleteButton = view.findViewById<Button>(R.id.delete_button)

            // 조건에 따라 delete_button 보이기/숨기기
            if (isInsert) {
                deleteButton.visibility = View.GONE 
            } else {
                deleteButton.visibility = View.VISIBLE 
            }

            if(!isInsert) view.findViewById<Button>(R.id.positive_button).text = "수정"

            // 버튼 설정
            view.findViewById<Button>(R.id.positive_button).setOnClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute

                val selectedTime = String.format("%02d:%02d", hour, minute)

                Log.d("SettingFragment",selectedTime)
                if(isInsert) timesViewModel.insertTime(selectedTime)
                else timesViewModel.updateTime(id!!,selectedTime)
                // 시간 설정 처리
                dismiss()
            }

            view.findViewById<Button>(R.id.negative_button).setOnClickListener {
                // 취소 처리
                dismiss()
            }

            view.findViewById<Button>(R.id.delete_button).setOnClickListener {
                // 추가 버튼 클릭 처리
                timesViewModel.deleteTime(id!!)
                dismiss()
            }

            builder.setView(view)
            return builder.create()
        }
    }
}