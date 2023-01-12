package com.danis.android.todo_list.presentation.adapters.notes

import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.databinding.NoteItemBinding
import com.danis.android.todo_list.domain.Note.CaseNote
import java.text.SimpleDateFormat
import java.util.*

class NoteViewHolder(
    val binding: NoteItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(caseNote: CaseNote) {
        binding.titleTextView.text = caseNote.title
        binding.lastUpdateTextView.text = getFormattedTime(caseNote.lastUpdate)
    }
    private fun getFormattedTime(time:Long):String{
        val formatter = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        return formatter.format(Date(time))
    }
    companion object{
        private const val TIME_PATTERN = "HH:mm dd.MM.yyyy"
    }
}