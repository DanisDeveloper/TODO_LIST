package com.danis.android.todo_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.viewModel.TODOViewModel

private const val sizeList = 31

class TodoFragment : Fragment() {
    private lateinit var binding:FragmentTodoBinding
    private var case = CaseTODO()
    private  var adapter = Adapter(emptyList())
    private val todoViewModel:TODOViewModel by lazy {
        ViewModelProvider(this).get(TODOViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater,container,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel.todoListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list->
            adapter = Adapter(list)
            binding.recyclerView.adapter = adapter
        })

    }

    override fun onResume() {
        super.onResume()

        binding.addButton.setOnClickListener {
            Toast.makeText(activity,"addButton",Toast.LENGTH_SHORT).show()
            todoViewModel.onClickAddButton()
        }
        binding.calendarButton.setOnClickListener {
            Toast.makeText(activity,"calendarButton",Toast.LENGTH_SHORT).show()
            todoViewModel.onClickCalendarButton()
        }
    }

    private inner class Holder(view:View):RecyclerView.ViewHolder(view){
        var binding = TodoItemBinding.bind(view)
        fun bind(case:CaseTODO){
            binding.checkBox.isChecked = case.isSolved
            binding.textView.text = case.todo
            itemView.setOnClickListener {

            }
        }

    }

    private inner class Adapter(val list:List<CaseTODO>):RecyclerView.Adapter<Holder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = layoutInflater.inflate(R.layout.todo_item,parent,false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.bind(list[position])
        }

        override fun getItemCount(): Int = list.size

    }

    companion object {
        fun newInstance() = TodoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}