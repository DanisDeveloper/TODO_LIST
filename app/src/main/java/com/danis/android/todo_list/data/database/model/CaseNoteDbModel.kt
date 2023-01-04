package com.danis.android.todo_list.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CaseNoteDbModel(
    var title: String,
    var text: String,
    var position: Int,
    @PrimaryKey val id: String
)
