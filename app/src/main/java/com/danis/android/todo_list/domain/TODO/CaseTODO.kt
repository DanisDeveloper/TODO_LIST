package com.danis.android.todo_list.domain.TODO

import java.io.Serializable


data class CaseTODO(
    val id: String,
    var date: Long,
    var todo: String,
    var isSolved: Boolean,
    var position: Int,
    var notificationTime: Long?,
    var notificationId:Int?
):Serializable
