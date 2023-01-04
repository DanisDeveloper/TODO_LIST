package com.danis.android.todo_list.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CaseTODODbModel(
    @PrimaryKey
    val id: String,
    var date: Long,
    var todo: String,
    var isSolved: Boolean,
    var priority: Int,
    var notificationTime: Long?,
    var notificationId:Int?
)
