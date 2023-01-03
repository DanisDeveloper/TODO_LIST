package com.danis.android.todo_list.presentation.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentNotesBinding
import com.danis.android.todo_list.databinding.NotesItemBinding
import com.danis.android.todo_list.domain.Note.CaseNote
import com.danis.android.todo_list.domain.TODO.CaseTODO
import com.danis.android.todo_list.presentation.activities.NoteDetailActivity
import com.danis.android.todo_list.presentation.adapters.notes.NotesAdapter
import com.danis.android.todo_list.presentation.viewmodel.NotesViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.*


class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding: FragmentNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentNotesBinding == null")

    private lateinit var notesAdapter: NotesAdapter
    private val notesViewModel: NotesViewModel by lazy {
        ViewModelProvider(this)[NotesViewModel::class.java]
    }
    private lateinit var snackbarUndo: Snackbar
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val caseNote = it.data?.getSerializableExtra("CASE") as CaseNote
                notesViewModel.updateNote(caseNote)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomSnackBar()
        setupRecyclerView()
        notesViewModel.notesListLiveData.observe(viewLifecycleOwner) { list ->
            notesAdapter.notesList = list
            if (list.isEmpty()) binding.notesNotFoundImageView.visibility = View.VISIBLE
            else binding.notesNotFoundImageView.visibility = View.GONE
        }
        notesViewModel.searchStringLiveData.observe(viewLifecycleOwner) {
            notesViewModel.loadListNotes()
        }

    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(notesViewModel.notesList)
        binding.recyclerView.adapter = notesAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        setupItemTouchHelper()
        setupClickListeners()
    }

    private fun setupItemTouchHelper() {
        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val caseNote = notesViewModel.notesList[viewHolder.adapterPosition]
                deleteNoteItemWithSnackBar(caseNote)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupClickListeners() {
        notesAdapter.onItemClickListener = {
            launcher?.launch(NoteDetailActivity.newIntent(requireActivity(), it))
        }
    }

    private fun setCustomSnackBar() {
        snackbarUndo = Snackbar.make(
            binding.coordinatorLayout,
            R.string.snackBarDeleteNote,
            Snackbar.LENGTH_LONG
        )
        snackbarUndo.setBackgroundTint(
            ContextCompat.getColor(
                requireActivity(),
                R.color.item_enabled_background
            )
        )
        snackbarUndo.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.text_color
            )
        )
        snackbarUndo.setActionTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.orange
            )
        )
        snackbarUndo.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    }

    private fun deleteNoteItemWithSnackBar(caseNote: CaseNote) {
        notesViewModel.deleteNote(caseNote)
        snackbarUndo.setAction(R.string.undo) {
            notesViewModel.insertNote(caseNote)
        }
        snackbarUndo.show()
    }


    override fun onStart() {
        super.onStart()
        binding.addButton.setOnClickListener {
            val caseNote = notesViewModel.getNewNote()
            notesViewModel.insertNote(caseNote)
            launcher?.launch(NoteDetailActivity.newIntent(requireActivity(), caseNote))
        }
        binding.notesSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesViewModel.searchNotes(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NotesFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }


}