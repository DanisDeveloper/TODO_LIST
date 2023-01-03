package com.danis.android.todo_list.presentation.adapters.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.databinding.NotesItemBinding
import com.danis.android.todo_list.domain.Note.CaseNote

class NotesAdapter(notesList: List<CaseNote>) : RecyclerView.Adapter<NotesViewHolder>() {
    var notesList: List<CaseNote> = notesList
        set(value) {
            val callback = NotesDiffCallback(notesList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var onItemClickListener: ((CaseNote) -> Unit)? = null

    //private var countOnBind = 0
    //private var countOnCreate = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        //Log.d("TAG","onCreate ${countOnCreate++}")
        val binding = NotesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        //Log.d("TAG","onBind ${countOnBind++}")
        val caseNote = notesList[position]
        holder.bind(caseNote)
        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(caseNote)
        }
    }

    override fun getItemCount(): Int = notesList.size
}

