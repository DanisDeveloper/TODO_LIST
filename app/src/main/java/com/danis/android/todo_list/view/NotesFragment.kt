package com.danis.android.todo_list.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentNotesBinding
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.NotesItemBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.viewModel.NotesViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

private const val NOTE_ID = "NOTE_ID"
private const val EXISTED = "EXISTED"

class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private var adapter = Adapter(emptyList())
    private val notesViewModel:NotesViewModel by lazy{
        ViewModelProvider(this).get(NotesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater,container,false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel.notesListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list->
            adapter = Adapter(list)
            binding.recyclerView.adapter = adapter
        })
    }

    override fun onResume() {
        super.onResume()
        binding.addButton.setOnClickListener {
            //notesViewModel.onClickAddButton()
            val intent = Intent(context,NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class Holder(view:View): RecyclerView.ViewHolder(view){
        var binding = NotesItemBinding.bind(view)
        fun bind(case: CaseNotes){
            itemView.setOnClickListener {
                val intent = Intent(context,NoteDetailActivity::class.java).apply {
                    val bundle = Bundle().apply {
                        putSerializable(NOTE_ID,case.id)
                    }
                    putExtras(bundle)
                    putExtra(EXISTED,true)
                }
                startActivity(intent)
            }
            binding.titleTextView.text = case.Title
        }
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

    companion object {
        fun newInstance() = NotesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}