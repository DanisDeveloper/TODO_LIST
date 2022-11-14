package com.danis.android.todo_list.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.CaseTODO

@Database(entities = [CaseTODO::class,CaseNotes::class],version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class DataBase: RoomDatabase() {
    abstract fun DAO():DAO
}