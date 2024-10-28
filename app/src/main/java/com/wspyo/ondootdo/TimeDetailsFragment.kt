package com.wspyo.ondootdo

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.wspyo.ondootdo.viewModel.TimesViewModel

class TimeDetailsFragment() : DialogFragment() {

    private val timesViewModel : TimesViewModel by activityViewModels()

    // newInstance 메서드: 파라미터를 넘길 때 사용
    companion object {
        fun newInstance(id: Int, isInsert: Boolean): TimeDetailsFragment {
            val fragment = TimeDetailsFragment()

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
        val view = inflater.inflate(R.layout.fragment_time_details, null)

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
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명 설정
        return dialog
    }

}