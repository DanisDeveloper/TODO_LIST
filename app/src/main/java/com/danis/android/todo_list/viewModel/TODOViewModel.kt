package com.danis.android.todo_list.viewModel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.Repository
import com.danis.android.todo_list.view.MainActivity
import java.util.*

class TODOViewModel:ViewModel() {
    private val repository = Repository.getInstance()
    private var date = getDate(Date())
    val todoListLiveData = repository.getTODOList(date)

    fun onClickAddButton(){
        repository.insertTODO(CaseTODO())
    }
    fun onClickCalendarButton(){
        Log.d("TAG","onClickCalendar")
        //TODO
    }

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