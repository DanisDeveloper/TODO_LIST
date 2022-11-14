package com.danis.android.todo_list.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class CaseTODO(@PrimaryKey val id:UUID= UUID.randomUUID(),
                    var date:Date=getDate(),
                    var todo:String="",
                    var isSolved:Boolean=false)
