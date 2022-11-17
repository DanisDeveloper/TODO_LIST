package com.danis.android.todo_list.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentNotesBinding
import com.danis.android.todo_list.databinding.NotesItemBinding
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.viewModel.NotesViewModel
import java.util.*

private const val NOTE_ID = "NOTE_ID"
private const val CHOICE_DIALOG_TAG = "CHOICE_DIALOG_TAG"
private const val CHOICE_DIALOG_REQUEST_CODE = 1

class NotesFragment : Fragment(),ChoiceDialogFragment.CallBack {
    private lateinit var binding: FragmentNotesBinding
    private var adapter = Adapter(emptyList())
    private val notesViewModel:NotesViewModel by lazy{
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }
    private var currentCase = CaseNotes()
    private var NotesList:List<CaseNotes> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel.searchNotes("")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater,container,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition
                Collections.swap(NotesList,startPosition,endPosition)
                NotesList[startPosition].position = startPosition
                NotesList[endPosition].position = endPosition
                adapter.notifyItemMoved(startPosition,endPosition)
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel.notesListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list->
            NotesList = list
            adapter = Adapter(list)
            binding.recyclerView.adapter = adapter
            if(list.isEmpty()) binding.notesNotFoundImageView.visibility = View.VISIBLE
            else binding.notesNotFoundImageView.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        binding.addButton.setOnClickListener {
            notesViewModel.saveNotesList(NotesList)
            val temp_case = CaseNotes(position = NotesList.size)
            notesViewModel.onClickAddButton(temp_case)
            startNote(temp_case.id)
        }
        binding.notesSearchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                notesViewModel.searchNotes(newText?:"")
                return true
            }
        })
    }

    override fun onStop() {
        super.onStop()
        notesViewModel.saveNotesList(NotesList)
    }

    private inner class Holder(view:View): RecyclerView.ViewHolder(view){
        var binding = NotesItemBinding.bind(view)
        fun bind(case: CaseNotes){
            binding.titleTextView.text = case.Title
            itemView.setOnClickListener {
                startNote(case.id)
            }
            // второй - это костыль,т.к. новое activity не открывалось по нажатию на textView
            binding.titleTextView.setOnClickListener {
                startNote(case.id)
            }
            binding.deleteImageView.setOnClickListener{
                activity?.let {
                    ChoiceDialogFragment.newInstance().apply {
                        setTargetFragment(this@NotesFragment,CHOICE_DIALOG_REQUEST_CODE)
                        show(it.supportFragmentManager,CHOICE_DIALOG_TAG)
                    }
                }
                currentCase = case
                notesViewModel.saveNotesList(NotesList)
            }
        }

    }
    fun startNote(id:UUID){
        val intent = Intent(context,NoteDetailActivity::class.java).apply {
            val bundle = Bundle().apply {
                putSerializable(NOTE_ID,id)
            }
            putExtras(bundle)
        }
        startActivity(intent)
    }
    private inner class Adapter(val list:List<CaseNotes>): RecyclerView.Adapter<Holder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = layoutInflater.inflate(R.layout.notes_item,parent,false)
            return Holder(view)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(list[position])
        }
        override fun getItemCount(): Int  = list.size
    }

    override fun onButtonClick(choice: Boolean) {
        if(choice){
            notesViewModel.deleteNote(currentCase)
            for(elem in currentCase.position until NotesList.size){
                NotesList[elem].position--
            }
            notesViewModel.saveNotesList(NotesList)
        }
    }

    companion object {
        fun newInstance() = NotesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}