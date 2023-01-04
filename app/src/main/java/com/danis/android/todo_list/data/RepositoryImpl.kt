package com.danis.android.todo_list.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.danis.android.todo_list.data.database.AppDataBase
import com.danis.android.todo_list.data.mapper.Mapper
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.Repository
import com.danis.android.todo_list.domain.TODO.CaseTODO


class RepositoryImpl(application: Application) : Repository {

    private val DAO = AppDataBase.getInstance(application).DAO()
    private val mapper = Mapper()

    override suspend fun getTODOList(date: Long): List<CaseTODO> = DAO.getTODOList(date).map {
        mapper.mapCaseTODODbModelToCaseTODO(it)
    }


    override suspend fun getTODOItem(id: String): CaseTODO {
        return mapper.mapCaseTODODbModelToCaseTODO(DAO.getTODOItem(id))
    }

    override suspend fun insertTODOItem(caseTODO: CaseTODO) {
        DAO.insertTODO(mapper.mapCaseTODOToCaseTODODbModel(caseTODO))
    }

    override suspend fun updateTODOList(list: List<CaseTODO>) {
        DAO.updateTODOList(list.map {
            mapper.mapCaseTODOToCaseTODODbModel(it)
        })
    }

    override suspend fun updateTODOItem(caseTODO: CaseTODO) {
        DAO.updateTODOItem(
            mapper.mapCaseTODOToCaseTODODbModel(caseTODO)
        )
    }

    override suspend fun deleteTODOItem(caseTODO: CaseTODO) {
        DAO.deleteTODO(mapper.mapCaseTODOToCaseTODODbModel(caseTODO))
    }

    override suspend fun getNoteList(searchQuery: String): List<CaseNote> {
        return DAO.getNotesListLIKE(searchQuery).map {
            mapper.mapCaseNoteDbModelToCaseNote(it)
        }
    }

    override suspend fun insertNoteItem(caseNote: CaseNote) {
        DAO.insertNote(mapper.mapCaseNoteToCaseNoteDbModel(caseNote))
    }

    override suspend fun updateNoteItem(caseNote: CaseNote) {
        DAO.updateNote(mapper.mapCaseNoteToCaseNoteDbModel(caseNote))
    }


    override suspend fun deleteNoteItem(caseNote: CaseNote) {
        DAO.deleteNote(mapper.mapCaseNoteToCaseNoteDbModel(caseNote))
    }


}