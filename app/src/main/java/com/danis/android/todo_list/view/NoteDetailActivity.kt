package com.danis.android.todo_list.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNoteDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note_detail)
    }
}