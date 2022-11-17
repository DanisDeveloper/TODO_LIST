package com.danis.android.todo_list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.Repository
import com.danis.android.todo_list.model.getDate
import java.util.*

class TODOViewModel:ViewModel() {
    private val repository = Repository.getInstance()
    private var dateLiveData = MutableLiveData<Date>()
    val todoListLiveData:LiveData<List<CaseTODO>> = Transformations.switchMap(dateLiveData){
        repository.getTODOList(it)
    }
    var currentDate:Date = getDate()

    fun loadTODOList(date:Date){
        dateLiveData.value = date
    }
    fun onClickAddButton(date: Date,position:Int){
        repository.insertTODO(CaseTODO(date=date, position = position))
    }
    fun saveTODOList(list:List<CaseTODO>){
        repository.updateTODO(list)
    }
    fun deleteTodo(case:CaseTODO){
        repository.deleteTODO(case)
    }

}
