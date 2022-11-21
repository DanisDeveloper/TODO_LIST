package com.danis.android.todo_list.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.CaseTODO
import java.util.*

@Dao
interface DAO {
    @Query("SELECT * FROM CaseTODO WHERE date=(:date) ORDER BY position")
    fun getTODOList(date:Date):LiveData<List<CaseTODO>>

    @Insert
    fun insertTODO(case:CaseTODO)

    @Update
    fun updateTODOList(list:List<CaseTODO>)

    @Delete
    fun deleteTODO(case:CaseTODO)


    @Query("SELECT * FROM CaseNotes WHERE id=(:id)")
    fun getNote(id:UUID):LiveData<CaseNotes?>

    @Query("SELECT * FROM CaseNotes WHERE Title LIKE (:searchQuery) ORDER BY position")
    fun getNotesListLIKE(searchQuery:String):LiveData<List<CaseNotes>>

    @Insert
    fun insertNotes(case:CaseNotes)

    @Update
    fun updateNote(case: CaseNotes)

    @Update
    fun updateNotes(list:List<CaseNotes>)

    @Delete
    fun deleteNote(case:CaseNotes)

}