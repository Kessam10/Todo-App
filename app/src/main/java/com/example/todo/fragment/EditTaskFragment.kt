//package com.example.todo.fragment
//
//import android.app.DatePickerDialog
//import android.app.TimePickerDialog
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import com.example.todo.R
//import com.example.todo.callback.OnTaskAddedListener
//import com.example.todo.clearTime
//import com.example.todo.dataBase.TaskDatabase
//import com.example.todo.dataBase.model.Task
//import com.example.todo.databinding.FragmentEditTaskBinding
//import java.util.Calendar
//
//class EditTaskFragment : Fragment() {
//    lateinit var binding: FragmentEditTaskBinding
//    lateinit var calendar: Calendar
//    var isDateSelected = false
//    var isTimeSelected = false
//    var onTaskAddedListener: OnTaskAddedListener? = null
//    private var task: Task? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentEditTaskBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize the calendar property
//        calendar = Calendar.getInstance()
//
//        // Retrieve the task from arguments
//
//        val toolbar = binding.toolbar
//
//        // Ensure the activity is an instance of AppCompatActivity
//        (activity as? AppCompatActivity)?.let { activity ->
//            activity.setSupportActionBar(toolbar)
//            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        }
//        toolbar.setNavigationOnClickListener {
//            Log.d("EditTaskFragment", "Toolbar navigation clicked")
//            // Use the FragmentManager to handle back navigation
//            parentFragmentManager.popBackStack()
//        }
//        initViews()
//    }
//
//    fun initViews() {
//        task?.let {
//            binding.titleEditText.setText(it.title)
//            binding.detailsEditText.setText(it.description)
//            binding.date.text = it.date.toString()
//            binding.time.text = it.time
//            calendar.time = it.date
//        }
//
//        binding.saveBtn.setOnClickListener {
//            Log.d("EditTaskFragment", "Save button clicked")
//            if (validateField()) {
//                calendar.clearTime()
//                val updatedTask = Task(
//                    task?.id,
//                    binding.titleEditText.text.toString(),
//                    binding.detailsEditText.text.toString(),
//                    binding.date.text.toString(),
//                    calendar.time,
//                )
//
//                TaskDatabase
//                    .getInstance(requireContext())
//                    .getTaskDao()
//                    .updateTask(updatedTask)
//                onTaskAddedListener?.onTaskUpdated()
//                Log.d("EditTaskFragment", "Task updated and navigating back")
//                parentFragmentManager.popBackStack()
//            }
//        }
//
//        binding.time.setOnClickListener {
//            val timePicker = TimePickerDialog(requireContext(),
//                { view, hourOfDay, minute ->
//                    isTimeSelected = true
//                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
//                    calendar.set(Calendar.MINUTE, minute)
//                    var hour = hourOfDay % 12
//                    if (hour == 0) hour = 12
//                    val AM_PM = if ((hourOfDay > 12)) "PM" else "AM"
//                    binding.time.text = "${hour}:${minute} $AM_PM"
//                },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                false
//            )
//            timePicker.show()
//        }
//
//        binding.date.setOnClickListener {
//            Log.d("EditTaskFragment", "Date clicked")
//            val datePicker = DatePickerDialog(requireContext())
//            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
//                isDateSelected = true
//                calendar.set(Calendar.YEAR, year)
//                calendar.set(Calendar.MONTH, month)
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//
//                binding.date.text = "$dayOfMonth/${month + 1}/$year"
//            }
//            datePicker.show()
//        }
//    }
//
//    fun validateField(): Boolean {
//        if (binding.titleEditText.text.isEmpty() || binding.titleEditText.text.isBlank()) {
//            binding.titleEditText.error = getString(R.string.required)
//            return false
//        } else {
//            binding.titleEditText.error = null
//        }
//        if (binding.detailsEditText.text.isEmpty() || binding.detailsEditText.text.isBlank()) {
//            binding.detailsEditText.error = getString(R.string.required)
//            return false
//        } else {
//            binding.detailsEditText.error = null
//        }
//        if (!isTimeSelected) {
//            Toast.makeText(requireContext(), getString(R.string.select_time), Toast.LENGTH_SHORT).show()
//            return false
//        }
//        if (!isDateSelected) {
//            Toast.makeText(requireContext(), getString(R.string.select_date), Toast.LENGTH_SHORT).show()
//            return false
//        }
//        return true
//    }
//}
package com.example.todo.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.callback.OnTaskAddedListener
import com.example.todo.clearTime
import com.example.todo.dataBase.TaskDatabase
import com.example.todo.dataBase.model.Task
import com.example.todo.databinding.FragmentEditTaskBinding
import java.util.Calendar

class EditTaskFragment : Fragment() {

    lateinit var binding: FragmentEditTaskBinding
    lateinit var calendar: Calendar
    var onTaskAddedListener: OnTaskAddedListener? = null
    var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = arguments?.getSerializable("task") as Task?
        calendar = Calendar.getInstance()

        task?.let {
            binding.titleEditText.setText(it.title)
            binding.detailsEditText.setText(it.description)
            binding.time.setText(it.time)
            calendar.time = it.date!!
            binding.date.text =
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${
                    calendar.get(Calendar.YEAR)
                }"

            val toolbar = binding.toolbar

            // Ensure the activity is an instance of AppCompatActivity
            (activity as? AppCompatActivity)?.let { activity ->
                activity.setSupportActionBar(toolbar)
                activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            toolbar.setNavigationOnClickListener {
                Log.d("EditTaskFragment", "Toolbar navigation clicked")
                // Use the FragmentManager to handle back navigation
                parentFragmentManager.popBackStack()
            }
        }

        initViews()
    }

    fun initViews() {
        binding.saveBtn.setOnClickListener {
            if (validateField()) {
                calendar.clearTime()
                task?.let {
                    it.title = binding.titleEditText.text.toString()
                    it.description = binding.detailsEditText.text.toString()
                    it.date = calendar.time

                    TaskDatabase.getInstance(requireContext()).getTaskDao().updateTask(it)
                    onTaskAddedListener?.onTaskUpdated()
                    parentFragmentManager.popBackStack()
                }
            }
        }

        binding.date.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.date.text = "$dayOfMonth/${month + 1}/$year"
            }
            datePicker.show()
        }

        binding.time.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    var hour = hourOfDay % 12
                    if (hour == 0) hour = 12
                    val AM_PM = if (hourOfDay > 12) "PM" else "AM"
                    binding.time.text = "${hour}:${minute} $AM_PM"
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
        if (binding.detailsEditText.text.isEmpty() || binding.detailsEditText.text.isBlank()) {
            binding.detailsEditText.error = getString(R.string.required)
            return false
        } else {
            binding.detailsEditText.error = null
        }
        return true
    }
}
