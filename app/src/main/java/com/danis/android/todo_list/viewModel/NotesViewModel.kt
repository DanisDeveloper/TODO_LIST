package com.danis.android.todo_list.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.Repository

class NotesViewModel: ViewModel() {
    private val repository = Repository.getInstance()
    private var searchString = MutableLiveData<String>()
    val notesListLiveData = Transformations.switchMap(searchString){
        repository.getNotesListLIKE(it)
    }

    fun onClickAddButton(case:CaseNotes) {
        repository.insertNotes(case)
    }
    fun searchNotes(searchQuery:String=""){
        searchString.value = "%${searchQuery}%"
    }
    fun saveNotesList(list:List<CaseNotes>){
        repository.updateNotes(list)
    }
    fun deleteNote(case:CaseNotes){
        repository.deleteNote(case)
    }
}