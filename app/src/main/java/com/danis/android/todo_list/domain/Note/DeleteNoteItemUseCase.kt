package com.danis.android.todo_list.domain.Note

import com.danis.android.todo_list.domain.Repository

class DeleteNoteItemUseCase(private val repository: Repository) {
    suspend fun deleteNoteItem(caseNote: CaseNote){
        repository.deleteNoteItem(caseNote)
    }
}