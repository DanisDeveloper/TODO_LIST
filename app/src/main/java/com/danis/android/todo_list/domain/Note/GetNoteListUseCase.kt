package com.danis.android.todo_list.domain.Note

import androidx.lifecycle.LiveData
import com.danis.android.todo_list.domain.Repository

class GetNoteListUseCase(private val repository: Repository) {
    suspend fun getNoteList(searchQuery:String):List<CaseNote>{
        return repository.getNoteList(searchQuery)
    }
}