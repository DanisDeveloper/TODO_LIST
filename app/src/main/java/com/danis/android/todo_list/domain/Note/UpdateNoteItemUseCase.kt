package com.danis.android.todo_list.domain.Note

import com.danis.android.todo_list.domain.Repository

class UpdateNoteItemUseCase(private val repository: Repository) {
    suspend fun updateNoteItem(caseNote:CaseNote){
        repository.updateNoteItem(caseNote)
    }
}