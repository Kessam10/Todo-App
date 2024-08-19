package com.example.todo.dataBase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    var id:Int?,

    var title:String? = null,

    var description:String? = null,

    val time:String? = null,

    var date:Date? = null,

    var isDone:Boolean?=null,
): Serializable
