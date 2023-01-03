package com.danis.android.todo_list.domain.Note

import com.danis.android.todo_list.domain.Repository

class InsertNoteItemUseCase(private val repository: Repository) {
    suspend fun insertNoteItem(caseNote: CaseNote){
        repository.insertNoteItem(caseNote)
    }
}