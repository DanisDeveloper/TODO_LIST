package com.danis.android.todo_list.viewModel

import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.Repository

class NotesViewModel: ViewModel() {
    private val repository = Repository.getInstance()
    val notesListLiveData = repository.getNotesList()
    fun onClickAddButton(case:CaseNotes) {
        repository.insertNotes(case)
    }
}