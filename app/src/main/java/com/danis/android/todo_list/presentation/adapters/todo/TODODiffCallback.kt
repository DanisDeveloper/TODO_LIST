package com.danis.android.todo_list.presentation.adapters.todo

import androidx.recyclerview.widget.DiffUtil
import com.danis.android.todo_list.domain.TODO.CaseTODO

class TODODiffCallback(private val oldList: List<CaseTODO>, private val newList: List<CaseTODO>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        (oldList[oldItemPosition].id == newList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        (oldList[oldItemPosition] == newList[newItemPosition])
}