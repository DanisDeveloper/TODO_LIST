package com.danis.android.todo_list.data.database

import androidx.room.*
import com.danis.android.todo_list.data.database.model.CaseNoteDbModel
import com.danis.android.todo_list.data.database.model.CaseTODODbModel

@Dao
interface DAO {
    @Query("SELECT * FROM CaseTODODbModel WHERE date=(:date) ORDER BY priority DESC")
    suspend fun getTODOList(date:Long): List<CaseTODODbModel>

    @Query("SELECT * FROM CaseTODODbModel WHERE id=(:id) LIMIT 1")
    suspend fun getTODOItem(id:String):CaseTODODbModel

    @Insert
    suspend fun insertTODO(case: CaseTODODbModel)

    @Update
    suspend fun updateTODOList(list:List<CaseTODODbModel>)

    @Update
    suspend fun updateTODOItem(case: CaseTODODbModel)

    @Delete
    suspend fun deleteTODO(case: CaseTODODbModel)



    @Query("SELECT * FROM CaseNoteDbModel WHERE title LIKE (:searchQuery) ORDER BY lastUpdate DESC")
    suspend fun getNotesListLIKE(searchQuery:String):List<CaseNoteDbModel>

    @Insert
    suspend fun insertNote(case: CaseNoteDbModel)

    @Update
    suspend fun updateNote(case: CaseNoteDbModel)

    @Delete
    suspend fun deleteNote(case: CaseNoteDbModel)

}