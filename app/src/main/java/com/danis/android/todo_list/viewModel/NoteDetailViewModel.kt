package com.danis.android.todo_list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.Repository
import java.util.*

class NoteDetailViewModel:ViewModel() {
    private val repository = Repository.getInstance()
    private var noteId = MutableLiveData<UUID>()
    val noteLiveData: LiveData<CaseNotes?> = Transformations.switchMap(noteId){
        repository.getNote(it)
    }
    fun loadNote(id:UUID){
        noteId.value = id
    }
    fun updateNote(case:CaseNotes){
        repository.updateNote(case)
    }

}