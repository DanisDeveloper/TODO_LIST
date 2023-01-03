package com.danis.android.todo_list.domain.Note

import androidx.lifecycle.LiveData
import com.danis.android.todo_list.domain.Repository
import java.util.*

class GetNoteItemUseCase(private val repository: Repository) {
    suspend fun getNoteItem(noteId:String):CaseNote{
        return repository.getNoteItem(noteId)
    }
}