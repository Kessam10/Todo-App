package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.dataBase.dao.TaskDao
import com.example.todo.dataBase.model.Task
import com.example.todo.databinding.ItemTaskBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(
    private var taskList: List<Task>,
     var taskDao: TaskDao,
    private val onTaskClickListener: OnTaskClickListener // Add the listener here
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTime.text = task.time
            binding.taskTitle.text = task.title

            // Set click listener for the entire item
            itemView.setOnClickListener {
                onTaskClickListener.onTaskClick(task) // Notify the listener
            }

            binding.taskCheck.setImageResource(
                if (task.isDone!!) {
                    R.drawable.is_done
                } else {
                    R.drawable.check
                }
            )
            if (task.isDone!!) {
                binding.verticalLine.setBackgroundColor(ContextCompat.getColor(binding.taskTitle.context, R.color.green))
                binding.taskTitle.setTextColor(ContextCompat.getColor(binding.taskTitle.context, R.color.green))
            } else {
                binding.verticalLine.setBackgroundColor(ContextCompat.getColor(binding.taskTitle.context, R.color.blue))
                binding.taskTitle.setTextColor(ContextCompat.getColor(binding.taskTitle.context, R.color.blue))
            }

            binding.taskCheck.setOnClickListener {
                task.isDone = !task.isDone!!
                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.updateTask(task) // Update task in the database
                    // Switch back to the Main thread to notify adapter
                    CoroutineScope(Dispatchers.Main).launch {
                        notifyItemChanged(bindingAdapterPosition) // Notify adapter about the change
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = taskList[position]
        holder.bind(item)
    }

    fun updateList(tasks: List<Task>) {
        taskList = tasks
        notifyDataSetChanged()
    }

    fun getTaskAtPosition(position: Int): Task {
        return taskList[position]
    }

    fun removeTaskAtPosition(position: Int) {
        taskList = taskList.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
    }

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
    }
}
