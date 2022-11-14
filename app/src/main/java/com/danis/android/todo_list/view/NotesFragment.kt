package com.danis.android.todo_list.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentNotesBinding
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.NotesItemBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesFragment : Fragment() {
    private lateinit var binding: FragmentNotesBinding
    private var title = "Example"
    private var adapter = Adapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private inner class Holder(view:View): RecyclerView.ViewHolder(view){
        var binding = NotesItemBinding.bind(view)
        fun bind(position: Int){
            binding.titleTextView.text = "$title ${position}"
            itemView.setOnClickListener {
                Toast.makeText(activity,"$title ${position}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class Adapter: RecyclerView.Adapter<Holder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = layoutInflater.inflate(R.layout.notes_item,parent,false)
            return Holder(view)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(position)
        }
        override fun getItemCount(): Int  = 30
    }

    companion object {
        fun newInstance() = NotesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}