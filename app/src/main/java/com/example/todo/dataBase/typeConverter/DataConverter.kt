package com.example.todo.dataBase.typeConverter

import androidx.room.TypeConverter
import java.util.Date

class DataConverter {

    @TypeConverter
    fun fromDate(date:Date):Long{
        return date.time
    }

    @TypeConverter
    fun toDate(timeStamp:Long):Date{
        return Date(timeStamp)
    }
}