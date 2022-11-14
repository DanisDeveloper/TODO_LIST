package com.danis.android.todo_list.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.danis.android.todo_list.model.database.DataBase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "TODO_DATABASE"

class Repository(context:Context) {
    private val database:DataBase = Room.databaseBuilder(
        context.applicationContext,
        DataBase::class.java,
        DATABASE_NAME
    ).build()
    private val DAO = database.DAO()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTODOList(date:Date):LiveData<List<CaseTODO>> = DAO.getTODOList(date)
    fun insertTODO(case:CaseTODO) {
        executor.execute{
            DAO.insertTODO(case)
        }
    }
    fun updateTODO(case:CaseTODO) {
        executor.execute{
            DAO.updateTODO(case)
        }
    }

    fun getNotesList():LiveData<List<CaseNotes>> = DAO.getNotesList()
    fun getNote(id:UUID):LiveData<CaseNotes> = DAO.getNote(id)


    companion object{
        private var INSTANCE:Repository? = null
        fun inizialize(context: Context){
            if(INSTANCE==null)
                INSTANCE = Repository(context)
        }
        fun getInstance():Repository{
            return INSTANCE?: throw IllegalStateException("Repository must be initialized!")
        }
    }
}