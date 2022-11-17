package com.danis.android.todo_list.model.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.CaseTODO
import java.util.*

@Dao
interface DAO {
    @Query("SELECT * FROM CaseTODO WHERE date=(:date)")
    fun getTODOList(date:Date):LiveData<List<CaseTODO>>

    @Insert
    fun insertTODO(case:CaseTODO)

    @Update
    fun updateTODO(list:List<CaseTODO>)

    @Delete
    fun deleteTODO(case:CaseTODO)


    @Query("SELECT * FROM CaseNotes")
    fun getNotesList():LiveData<List<CaseNotes>>

    @Query("SELECT * FROM CaseNotes WHERE id=(:id)")
    fun getNote(id:UUID):LiveData<CaseNotes?>

    @Query("SELECT * FROM CaseNotes WHERE Title LIKE (:searchQuery)")
    fun getNotesListLIKE(searchQuery:String):LiveData<List<CaseNotes>>

    @Insert
    fun insertNotes(case:CaseNotes)

    @Update
    fun updateNotes(case: CaseNotes)
}