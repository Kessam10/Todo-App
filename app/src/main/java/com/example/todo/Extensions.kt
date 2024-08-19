package com.example.todo

import java.util.Calendar

fun Calendar.clearTime(){
    set(Calendar.HOUR,0)
    set(Calendar.MINUTE,0)
    set(Calendar.SECOND,0)
    set(Calendar.MILLISECOND,0)
}