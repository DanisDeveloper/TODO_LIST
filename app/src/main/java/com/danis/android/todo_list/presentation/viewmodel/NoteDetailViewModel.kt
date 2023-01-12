package com.danis.android.todo_list.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.danis.android.todo_list.data.RepositoryImpl
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.Note.DeleteNoteItemUseCase
import com.danis.android.todo_list.domain.Note.UpdateNoteItemUseCase
import kotlinx.coroutines.launch

class NoteDetailViewModel(application: Application):AndroidViewModel(application) {
    private val repository = RepositoryImpl(application)
    private val updateNoteItemUseCase = UpdateNoteItemUseCase(repository)

    lateinit var inCaseNote: CaseNote
    lateinit var outCaseNote: CaseNote
    var savedCaseNote: CaseNote?=null

    fun saveNoteItem(){
        viewModelScope.launch {
            updateNoteItemUseCase.updateNoteItem(outCaseNote)
        }
    }
}