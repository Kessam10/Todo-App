package com.example.todo.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.todo.R
import com.example.todo.callback.OnTaskAddedListener
import com.example.todo.clearTime
import com.example.todo.dataBase.TaskDatabase
import com.example.todo.dataBase.model.Task
import com.example.todo.databinding.FragmentAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTaskBottomSheetFragment: BottomSheetDialogFragment() {

    lateinit var binding: FragmentAddTaskBinding
    lateinit var calendar: Calendar
    var isDateSelected = false
    var isTimeSelected = false
    var onTaskAddedListener: OnTaskAddedListener?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        initViews()
    }

    fun initViews() {
        binding.addButton.setOnClickListener {
            if (validateField()) {
                // Task creation logic
                calendar.clearTime()
                val task = Task(
                    null,
                    binding.titleEditText.text.toString(),
                    binding.descEditText.text.toString(),
                    binding.selectTaskTimeText.text.toString(),
                    calendar.time,
                    false
                )

                // Insert task into the database
                TaskDatabase
                    .getInstance(requireContext())
                    .getTaskDao()
                    .insertTask(task)

                // Notify the listener
                notifyTaskAdded()

                // Dismiss the bottom sheet
                dismiss()
            }
        }


        binding.selectTaskDateText.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                isDateSelected = true
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.selectTaskDateText.text = "$dayOfMonth/${month + 1}/$year"
            }
            datePicker.show()
        }

        binding.selectTaskTimeText.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(),
                { _, hourOfDay, minute ->
                    isTimeSelected = true
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    var hour = hourOfDay % 12
                    if (hour == 0) hour = 12
                    val AM_PM = if (hourOfDay > 12) "PM" else "AM"
                    binding.selectTaskTimeText.text = "${hour}:${minute} $AM_PM"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }
    }

    fun validateField(): Boolean {
        if (binding.titleEditText.text.isEmpty() || binding.titleEditText.text.isBlank()) {
            binding.titleEditText.error = getString(R.string.required)
            return false
        } else {
            binding.titleEditText.error = null
        }
        if (binding.descEditText.text.isEmpty() || binding.descEditText.text.isBlank()) {
            binding.descEditText.error = getString(R.string.required)
            return false
        } else {
            binding.descEditText.error = null
        }
        if (!isTimeSelected) {
            Toast.makeText(requireContext(), getString(R.string.select_time), Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isDateSelected) {
            Toast.makeText(requireContext(), getString(R.string.select_date), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun notifyTaskAdded() {
        parentFragmentManager.setFragmentResult("taskAdded", Bundle())
    }

}
