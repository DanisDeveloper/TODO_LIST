package com.danis.android.todo_list.domain.Note

import java.io.Serializable

data class CaseNote(
    val id: String,
    var title: String,
    var text: String,
    var lastUpdate: Long
):Serializable
