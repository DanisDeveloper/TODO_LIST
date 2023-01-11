package com.danis.android.todo_list.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.danis.android.todo_list.data.RepositoryImpl
import com.danis.android.todo_list.domain.TODO.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TODOViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)
    private val getTODOListUseCase = GetTODOListUseCase(repository)
    private val deleteTODOItemUseCase = DeleteTODOItemUseCase(repository)
    private val updateTODOListUseCase = UpdateTODOListUseCase(repository)
    private val updateTODOItemUseCase = UpdateTODOItemUseCase(repository)
    private val insertTODOItemUseCase = InsertTODOItemUseCase(repository)

    private var _dateLiveData = MutableLiveData<Date>()
    val dateLiveData: LiveData<Date>
        get() = _dateLiveData

    private var _todoListLiveData = MutableLiveData<List<CaseTODO>>()
    val todoListLiveData :LiveData<List<CaseTODO>>
        get() = _todoListLiveData

    var TODOList: List<CaseTODO> = emptyList()

    private var _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    init{
        _dateLiveData.value = getDate()
    }

    fun loadTODOList(date: Date = getDate()) {
        viewModelScope.launch {
            TODOList = getTODOListUseCase.getTODOList(date.time).sortedBy { it.isSolved }
            _todoListLiveData.value = TODOList
        }
        _formattedTime.value = getFormattedTime(date)
    }

    fun onClickAddButton() {
        val caseTODO = CaseTODO(
            UUID.randomUUID().toString(),
            _dateLiveData.value?.time!!,
            "",
            false,
            0,
            null,
            (Math.random()*MAX_RANDOM_VALUE).toInt()
        )
        viewModelScope.launch {
            //updateTODOListUseCase.updateTODOList(TODOList)
            insertTODOItemUseCase.insertTODOItem(caseTODO)
            loadTODOList(Date(caseTODO.date))
        }
    }

    fun insertTODOItem(caseTODO: CaseTODO){
        viewModelScope.launch {
            //updateTODOListUseCase.updateTODOList(TODOList)
            insertTODOItemUseCase.insertTODOItem(caseTODO)
            loadTODOList(Date(caseTODO.date))
        }
    }

    fun onClickCalendarButton(date:Date) {
        _dateLiveData.value = getDate(date)
        _formattedTime.value = getFormattedTime(date)
    }

    fun saveTODOList() {
        viewModelScope.launch {
            updateTODOListUseCase.updateTODOList(TODOList)
        }
    }

    fun deleteTodo(caseTODO: CaseTODO) {
        viewModelScope.launch {
            //updateTODOListUseCase.updateTODOList(TODOList)
            deleteTODOItemUseCase.deleteTODOItem(caseTODO)
            loadTODOList(Date(caseTODO.date))
        }
    }

    fun updateTODOItem(caseTODO: CaseTODO){
        viewModelScope.launch {
            //updateTODOListUseCase.updateTODOList(TODOList)
            updateTODOItemUseCase.updateTODOItem(caseTODO)
            loadTODOList(Date(dateLiveData.value?.time!!))
        }
    }

    fun changeEnabledState(caseTODO: CaseTODO){
        viewModelScope.launch {
            updateTODOItemUseCase.updateTODOItem(caseTODO.copy(isSolved = !caseTODO.isSolved))
            loadTODOList(Date(dateLiveData.value?.time!!))
        }
    }

    private fun getDate(date: Date = Date()): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.get(Calendar.DATE)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return GregorianCalendar(year, month, day, 0, 0, 0).time
    }

    private fun getFormattedTime(date: Date): String {
        val formatter = SimpleDateFormat("EEEE dd.MM.yy", Locale.getDefault())
        return formatter.format(date)
    }
    companion object{
        private const val MAX_RANDOM_VALUE = 10000000
    }
}
