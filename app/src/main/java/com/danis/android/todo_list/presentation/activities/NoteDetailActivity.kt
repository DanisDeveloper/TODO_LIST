package com.danis.android.todo_list.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.ActivityNoteDetailBinding
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.presentation.viewmodel.NoteDetailViewModel
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailBinding
    private val noteDetailViewModel: NoteDetailViewModel by lazy {
        ViewModelProvider(this)[NoteDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        binding.titleEditText.setText(noteDetailViewModel.inCaseNote.title)
        binding.contentEditText.setText(noteDetailViewModel.inCaseNote.text)
    }

    private fun parseIntent() {
        noteDetailViewModel.inCaseNote = intent.getSerializableExtra(NOTE_ID) as CaseNote
        noteDetailViewModel.outCaseNote = noteDetailViewModel.inCaseNote.copy()
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            titleEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    noteDetailViewModel.outCaseNote.title = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            contentEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    noteDetailViewModel.outCaseNote.text = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
        binding.toolbar.setNavigationOnClickListener {
            returnResult()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.okay ->{
                    binding.titleEditText.isEnabled = false
                    binding.titleEditText.isEnabled = true
                    binding.contentEditText.isEnabled = false
                    binding.contentEditText.isEnabled = true
                }
                R.id.undo -> {
                    if (noteDetailViewModel.inCaseNote != noteDetailViewModel.outCaseNote)
                        noteDetailViewModel.savedCaseNote = noteDetailViewModel.outCaseNote.copy()
                    binding.titleEditText.setText(noteDetailViewModel.inCaseNote.title)
                    binding.contentEditText.setText(noteDetailViewModel.inCaseNote.text)
                }
                R.id.redo -> {
                    if (noteDetailViewModel.savedCaseNote != null) {
                        if(noteDetailViewModel.inCaseNote == noteDetailViewModel.outCaseNote) {
                            binding.titleEditText.setText(noteDetailViewModel.savedCaseNote?.title)
                            binding.contentEditText.setText(noteDetailViewModel.savedCaseNote?.text)
                        }
                        noteDetailViewModel.savedCaseNote = null
                    }
                }
                R.id.share -> {
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, noteDetailViewModel.outCaseNote.title)
                        putExtra(Intent.EXTRA_TEXT, noteDetailViewModel.outCaseNote.text)
                        startActivity(Intent.createChooser(this, getString(R.string.share_via)))
                    }
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        returnResult()
        super.onBackPressed()
    }

    private fun returnResult() {
        setResult(RESULT_OK, Intent().putExtra(CASE, noteDetailViewModel.outCaseNote))
        finish()
    }

    override fun onStop() {
        super.onStop()
        noteDetailViewModel.saveNoteItem(noteDetailViewModel.outCaseNote)
    }

    companion object {
        private const val NOTE_ID = "NOTE_ID"
        const val CASE = "CASE"
        fun newIntent(context: Context, caseNote: CaseNote): Intent {
            return Intent(context, NoteDetailActivity::class.java).apply {
                putExtra(NOTE_ID, caseNote)
            }
        }
    }
}