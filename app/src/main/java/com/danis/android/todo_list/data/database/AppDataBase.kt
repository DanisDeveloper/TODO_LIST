package com.danis.android.todo_list.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.danis.android.todo_list.data.database.model.CaseNoteDbModel
import com.danis.android.todo_list.data.database.model.CaseTODODbModel

@Database(entities = [CaseTODODbModel::class, CaseNoteDbModel::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun DAO(): DAO

    companion object {
        private var DB: AppDataBase? = null
        private const val DATABASE_NAME = "TODO_DATABASE"
        private val LOCK = Any()

        fun getInstance(context: Context): AppDataBase {
            synchronized(LOCK) {
                DB?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    DATABASE_NAME
                ).addMigrations(migration_1_2)
                    .build()
                DB = instance
                return instance
            }
        }
    }
}
val migration_1_2 = object:Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE CaseTODO RENAME TO CaseTODODbModel")
        database.execSQL("ALTER TABLE CaseTODODbModel ADD COLUMN notificationId INTEGER DEFAULT NULL")
        database.execSQL("ALTER TABLE CaseNotes RENAME TO CaseNoteDbModel")
    }

}