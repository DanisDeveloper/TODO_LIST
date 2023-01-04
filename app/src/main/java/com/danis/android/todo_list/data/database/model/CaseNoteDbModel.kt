package com.danis.android.todo_list.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CaseNoteDbModel(
    @PrimaryKey val id: String,
    var title: String,
    var text: String,
    var position: Int

)
