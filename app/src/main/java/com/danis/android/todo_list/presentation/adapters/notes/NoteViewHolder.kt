package com.danis.android.todo_list.presentation.adapters.notes

import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.databinding.NoteItemBinding
import com.danis.android.todo_list.domain.Note.CaseNote

class NoteViewHolder(
    val binding: NoteItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(caseTODO: CaseNote) {
        binding.titleTextView.text = caseTODO.title
    }
}