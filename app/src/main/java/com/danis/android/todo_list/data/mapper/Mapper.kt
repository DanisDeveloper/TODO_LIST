package com.danis.android.todo_list.data.mapper

import com.danis.android.todo_list.data.database.model.CaseNoteDbModel
import com.danis.android.todo_list.data.database.model.CaseTODODbModel
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.TODO.CaseTODO

class Mapper {

    fun mapCaseNoteDbModelToCaseNote(CaseNoteDbModel:CaseNoteDbModel):CaseNote{
        return CaseNote(
            id = CaseNoteDbModel.id,
            title = CaseNoteDbModel.title,
            text = CaseNoteDbModel.text,
            lastUpdate = CaseNoteDbModel.lastUpdate
        )
    }

    fun mapCaseNoteToCaseNoteDbModel(caseNote:CaseNote):CaseNoteDbModel{
        return CaseNoteDbModel(
            id = caseNote.id,
            title = caseNote.title,
            text = caseNote.text,
            lastUpdate = caseNote.lastUpdate
        )
    }

    fun mapCaseTODODbModelToCaseTODO(CaseTODODbModel: CaseTODODbModel): CaseTODO {
        return CaseTODO(
            id = CaseTODODbModel.id,
            date = CaseTODODbModel.date,
            todo = CaseTODODbModel.todo,
            isSolved = CaseTODODbModel.isSolved,
            priority = CaseTODODbModel.priority,
            notificationTime = CaseTODODbModel.notificationTime,
            notificationId = CaseTODODbModel.notificationId
        )
    }

    fun mapCaseTODOToCaseTODODbModel(caseTODO:CaseTODO):CaseTODODbModel{
        return CaseTODODbModel(
            id = caseTODO.id,
            date = caseTODO.date,
            todo = caseTODO.todo,
            isSolved = caseTODO.isSolved,
            priority = caseTODO.priority,
            notificationTime = caseTODO.notificationTime,
            notificationId = caseTODO.notificationId
        )
    }
}