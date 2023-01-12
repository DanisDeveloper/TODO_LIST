package com.danis.android.todo_list.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.danis.android.todo_list.data.RepositoryImpl
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.Note.DeleteNoteItemUseCase
import com.danis.android.todo_list.domain.Note.GetNoteListUseCase
import com.danis.android.todo_list.domain.Note.InsertNoteItemUseCase
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val repository = RepositoryImpl(application)
    private val getNoteListUseCase = GetNoteListUseCase(repository)
    private val insertNoteItemUseCase = InsertNoteItemUseCase(repository)
    private val deleteNoteItemUseCase = DeleteNoteItemUseCase(repository)

    private var _searchStringLiveData = MutableLiveData<String>()
    val searchStringLiveData:LiveData<String>
        get() = _searchStringLiveData

    private var _notesListLiveData = MutableLiveData<List<CaseNote>>()
    val notesListLiveData:LiveData<List<CaseNote>>
    get() = _notesListLiveData

    var notesList:List<CaseNote> = emptyList()

    init{
        searchNotes()
    }

    fun loadListNotes(){
        viewModelScope.launch {
            notesList = getNoteListUseCase.getNoteList(_searchStringLiveData.value!!)
            _notesListLiveData.value = notesList
        }
    }

    fun insertNote(caseNote: CaseNote){
        viewModelScope.launch {
            insertNoteItemUseCase.insertNoteItem(caseNote)
            loadListNotes()
        }
    }
    fun searchNotes(searchQuery:String=""){
        _searchStringLiveData.value = "%${searchQuery}%"
    }
    fun deleteNote(caseNote: CaseNote){
       viewModelScope.launch {
           deleteNoteItemUseCase.deleteNoteItem(caseNote)
           loadListNotes()
       }
    }
    fun updateNote(caseNote:CaseNote) {
        viewModelScope.launch {
            repository.updateNoteItem(caseNote)
            loadListNotes()
        }
    }

    fun getNewNote(): CaseNote {
        return CaseNote(
            id = UUID.randomUUID().toString(),
            title = "",
            text = "",
            lastUpdate = Date().time
        )
    }
}