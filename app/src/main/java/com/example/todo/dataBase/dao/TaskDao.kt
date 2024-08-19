package com.example.todo.dataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo.dataBase.model.Task
import java.util.Date

// data access object
@Dao
interface TaskDao {
    @Insert
    fun insertTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM Task")
    fun getAllTasks():List<Task>

    @Query("SELECT * FROM TASK WHERE date = :date")
    fun getTaskByDate(date: Date): List<Task>
}