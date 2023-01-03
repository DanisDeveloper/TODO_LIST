package com.danis.android.todo_list.domain

import androidx.lifecycle.LiveData
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.TODO.CaseTODO
import java.util.*

interface Repository {
    suspend fun getTODOList(date:Long):List<CaseTODO>
    suspend fun getTODOItem(id:String):CaseTODO
    suspend fun insertTODOItem(caseTODO: CaseTODO)
    suspend fun updateTODOList(list:List<CaseTODO>)
    suspend fun updateTODOItem(caseTODO: CaseTODO)
    suspend fun deleteTODOItem(caseTODO: CaseTODO)

    suspend fun getNoteItem(noteId:String):CaseNote
    suspend fun getNoteList(searchQuery:String):List<CaseNote>
    suspend fun insertNoteItem(caseNote: CaseNote)
    suspend fun updateNoteItem(caseNote: CaseNote)
    suspend fun updateNoteList(list:List<CaseNote>)
    suspend fun deleteNoteItem(caseNote: CaseNote)
}