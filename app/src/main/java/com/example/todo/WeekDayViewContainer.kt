package com.example.todo

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer

class WeekDayViewContainer(val itemWeekDayView:View) :ViewContainer(itemWeekDayView){
    val weekDayTextView:TextView = itemWeekDayView.findViewById(R.id.week_day)
    val dayMonthTextView:TextView = itemWeekDayView.findViewById(R.id.day_in_month)

}