package com.example.todo.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todo.dataBase.dao.TaskDao
import com.example.todo.dataBase.model.Task
import com.example.todo.dataBase.typeConverter.DataConverter

@Database([Task::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class TaskDatabase:RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object{

       private var INSTANCE:TaskDatabase?=null
        private const val DATABASE_NAME = "Tasks Database"
        fun getInstance(context: Context):TaskDatabase{
           if (INSTANCE==null)
               INSTANCE = Room.databaseBuilder(context,TaskDatabase::class.java, DATABASE_NAME)
                   .fallbackToDestructiveMigration()  //
                   .allowMainThreadQueries()
                   .build()
            return  INSTANCE!!
        }
    }

}