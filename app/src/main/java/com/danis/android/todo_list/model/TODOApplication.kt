package com.danis.android.todo_list.model

import android.app.Application
import android.content.Context
import java.util.*

class TODOApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.inizialize(this)
    }
}

fun getDate(date:Date = Date()):Date{
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.get(Calendar.DATE)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return GregorianCalendar(year,month,day,0,0,0).time
}