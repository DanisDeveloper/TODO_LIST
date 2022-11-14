package com.danis.android.todo_list.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.Repository
import com.danis.android.todo_list.model.getDate
import java.util.*

class TODOViewModel:ViewModel() {
    private val repository = Repository.getInstance()
    private var date = getDate()
    val todoListLiveData = repository.getTODOList(date)

    fun onClickAddButton(){
        repository.insertTODO(CaseTODO())
    }
    fun onClickCalendarButton(date:Date){
        Log.d("TAG","onClickCalendar")
        //TODO
    }

}
