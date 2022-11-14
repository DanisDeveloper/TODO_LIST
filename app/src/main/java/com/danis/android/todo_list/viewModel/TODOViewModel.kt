package com.danis.android.todo_list.viewModel

import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.Repository
import java.util.*

class TODOViewModel:ViewModel() {
    private val repository = Repository.getInstance()
    private var date = getDate(Date())
    val todoListLiveData = repository.getTODOList(date)


}
fun getDate(date:Date):Date{
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.get(Calendar.DATE)
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return GregorianCalendar(year,month,day,0,0,0).time
}