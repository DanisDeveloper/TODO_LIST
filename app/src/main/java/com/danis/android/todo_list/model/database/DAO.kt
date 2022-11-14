package com.danis.android.todo_list.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.CaseTODO
import java.util.*

@Dao
interface DAO {
    @Query("SELECT * FROM CaseTODO WHERE date=(:date) ORDER BY priority DESC")
    fun getTODOList(date:Date):LiveData<List<CaseTODO>>

   /* @Query("SELECT COUNT(*) FROM CaseTODO GROUP BY date")
    fun getCountCaseTODO():LiveData<List<CaseTODO>>

    @Query("SELECT COUNT(*) FROM CaseTODO WHERE isSolved=1 GROUP BY date")
    fun getCountSolvesCaseTODO():LiveData<List<CaseTODO>>*/

    @Query("SELECT * FROM CaseNotes")
    fun getNotesList():LiveData<List<CaseNotes>>

    @Query("SELECT * FROM CaseNotes WHERE id=(:id)")
    fun getNote(id:UUID):LiveData<CaseNotes>
}