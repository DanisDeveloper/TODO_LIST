package com.danis.android.todo_list.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CaseNotes(@PrimaryKey val id: UUID=UUID.randomUUID(),
                     var Title:String="",
                     var text:String="",
                     var position:Int = 0)
