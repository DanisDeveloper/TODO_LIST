package com.danis.android.todo_list.domain.Note

import com.danis.android.todo_list.domain.Repository

class UpdateNoteListUseCase(private val repository: Repository) {
    suspend fun updateNoteList(list:List<CaseNote>){
        repository.updateNoteList(list)
    }
}