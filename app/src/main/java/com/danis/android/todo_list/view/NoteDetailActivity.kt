package com.danis.android.todo_list.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.ActivityNoteDetailBinding
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.viewModel.NoteDetailViewModel
import java.util.*


private const val NOTE_ID = "NOTE_ID"
private const val EXISTED = "EXISTED"

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNoteDetailBinding
    private val noteDetailViewModel: NoteDetailViewModel by lazy{
        ViewModelProvider(this).get(NoteDetailViewModel::class.java)
    }
    private var case = CaseNotes()
    private var existed :Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note_detail)
        val bundle = intent.extras
        bundle?.let {
            noteDetailViewModel.loadNote(bundle.getSerializable(NOTE_ID) as UUID)
        }

        existed = intent?.getBooleanExtra(EXISTED,false)
    }

    override fun onStart() {
        super.onStart()
        noteDetailViewModel.noteLiveData.observe(this, Observer {
            it?.let {
                case = it
                binding.titleEditText.setText(case.Title)
                binding.contentEditText.setText(case.text)
            }
        })
        binding.apply {
            titleEditText.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    case.Title = s.toString()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
            contentEditText.addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    case.text = s.toString()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

    }

    override fun onStop() {
        super.onStop()
        if(existed!!) noteDetailViewModel.updateNote(case)
        else noteDetailViewModel.saveNote(case)
    }
}