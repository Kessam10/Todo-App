package com.example.todo.fragment

import android.content.res.Configuration
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.WeekDayViewContainer
import com.example.todo.adapter.TaskAdapter
import com.example.todo.callback.OnTaskAddedListener
import com.example.todo.clearTime
import com.example.todo.dataBase.TaskDatabase
import com.example.todo.dataBase.model.Task
import com.example.todo.databinding.FragmentTaskBinding
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TasksFragment : Fragment(), TaskAdapter.OnTaskClickListener, OnTaskAddedListener {

    lateinit var binding: FragmentTaskBinding
    lateinit var adapter: TaskAdapter
    var selectedDate: LocalDate? = null
    lateinit var calendar: Calendar
    val taskDao by lazy { TaskDatabase.getInstance(requireContext()).getTaskDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()

        initWeekCalendarView()
        setupRecyclerView()
        getTasksFromDatabase()
        parentFragmentManager.setFragmentResultListener("taskAdded", viewLifecycleOwner) { _, _ ->
            refreshTasks()
        }

    }

    private fun setupRecyclerView() {
        binding.taskRecycleView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskAdapter(emptyList(), taskDao, this) // Pass the fragment as the listener
        binding.taskRecycleView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.taskRecycleView)
    }

    private val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val task = adapter.getTaskAtPosition(position)

            // Delete task from database
            CoroutineScope(Dispatchers.IO).launch {
                taskDao.deleteTask(task)
            }

            // Remove task from adapter
            adapter.removeTaskAtPosition(position)
            Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                .addActionIcon(R.drawable.ic_delete)
                .addCornerRadius(2, 15)
                .addSwipeRightLabel("Delete")
                .setSwipeRightLabelColor(R.color.white)
                .setSwipeRightLabelTextSize(2, 20F)
                .addSwipeRightPadding(2, 20F, 25F, 20F)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    fun getTasksFromDatabase() {
        val tasks = taskDao.getAllTasks()
        adapter.updateList(tasks)
        binding.taskRecycleView.adapter = adapter
    }

    private fun initWeekCalendarView() {
        bindWeekCalendarView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDate = LocalDate.now()
            val currentMonth = YearMonth.now()
            val startDate = currentMonth.minusMonths(100).atStartOfMonth()
            val endDate = currentMonth.plusMonths(100).atEndOfMonth()
            val firstDayOfWeek = firstDayOfWeekFromLocale(Locale.getDefault())

            binding.weeksCalendarView.setup(startDate, endDate, firstDayOfWeek)
            binding.weeksCalendarView.scrollToWeek(currentDate)
        }
    }

    private fun bindWeekCalendarView() {
        binding.weeksCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val blue = ResourcesCompat.getColor(resources, R.color.blue, null)
                    val white = ResourcesCompat.getColor(resources, R.color.white, null)
                    val black = ResourcesCompat.getColor(resources, R.color.black, null)

                    container.weekDayTextView.text = data.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    container.dayMonthTextView.text = "${data.date.dayOfMonth}"

                    // Determine night mode
                    val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    val textColor = when (nightModeFlags) {
                        Configuration.UI_MODE_NIGHT_YES -> white
                        Configuration.UI_MODE_NIGHT_NO -> black
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> black
                        else -> black
                    }

                    // Set colors based on whether the date is selected
                    if (data.date == selectedDate) {
                        container.dayMonthTextView.setTextColor(blue)
                        container.weekDayTextView.setTextColor(blue)
                    } else {
                        container.dayMonthTextView.setTextColor(textColor)
                        container.weekDayTextView.setTextColor(textColor)
                    }

                    container.view.setOnClickListener {
                        selectedDate = data.date
                        binding.weeksCalendarView.notifyWeekChanged(data)
                        val date = data.date
                        calendar.set(Calendar.YEAR, date.year)
                        calendar.set(Calendar.MONTH, date.month.value - 1)
                        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                        calendar.clearTime()
                        getTaskByDate(calendar.time)
                    }
                }
            }

            override fun create(view: View): WeekDayViewContainer {
                return WeekDayViewContainer(view)
            }
        }

        binding.weeksCalendarView.weekScrollListener = { week: Week ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val month = week.days[0].date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                binding.monthNameText.text = month
            }
        }
    }

     fun getTaskByDate(date: Date) {
        val tasks = taskDao.getTaskByDate(date)
        adapter.updateList(tasks)
    }


    override fun onTaskClick(task: Task) {
        val editTaskFragment = EditTaskFragment().apply {
            arguments = Bundle().apply {
                putSerializable("task", task)
            }
            onTaskAddedListener = this@TasksFragment
        }
        showFragment(editTaskFragment)
    }

    override fun onTaskAdded() {
        getTasksFromDatabase()
    }

    override fun onTaskUpdated() {
        getTasksFromDatabase()
    }

    fun showFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.edit_task, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun refreshTasks() {
        if (selectedDate == null) {
            getTasksFromDatabase()
        } else {
            getTaskByDate(calendar.time)
        }
    }
}
