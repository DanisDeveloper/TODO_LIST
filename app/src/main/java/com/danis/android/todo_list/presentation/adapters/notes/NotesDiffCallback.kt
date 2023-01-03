package com.danis.android.todo_list.presentation.adapters.notes

import androidx.recyclerview.widget.DiffUtil
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.TODO.CaseTODO

class NotesDiffCallback(private val oldList: List<CaseNote>, private val newList: List<CaseNote>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        (oldList[oldItemPosition].id == newList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        (oldList[oldItemPosition] == newList[newItemPosition])
}