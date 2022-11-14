package com.danis.android.todo_list.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.model.CaseNotes
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.Repository
import com.danis.android.todo_list.viewModel.TODOViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val sizeList = 31

class TodoFragment : Fragment() {
    private lateinit var binding:FragmentTodoBinding
    private var date = Date()
    private  var adapter = Adapter()
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


    private inner class Holder(view:View):RecyclerView.ViewHolder(view){
        var binding = TodoItemBinding.bind(view)
        fun bind(){
            val formatter = SimpleDateFormat("dd.MM.yyyy",Locale.getDefault())
            binding.dateTextView.text = formatter.format(date)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val day_date = calendar.get(Calendar.DAY_OF_MONTH)
            val month_date = calendar.get(Calendar.MONTH)
            val year_date = calendar.get(Calendar.YEAR)
            calendar.time = Date()
            val day_now = calendar.get(Calendar.DAY_OF_MONTH)
            val month_now = calendar.get(Calendar.MONTH)
            val year_now = calendar.get(Calendar.YEAR)
            if((day_date == day_now) and (month_date == month_now) and (year_date == year_now)) {
                binding.todoItemCardView.setCardBackgroundColor(resources.getColor(R.color.orange))
                binding.dateTextView.setTextSize(25.0f)
                binding.countTextView.setTextSize(25.0f)
            }

            itemView.setOnClickListener {

            }
        }

    }

    private inner class Adapter:RecyclerView.Adapter<Holder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = layoutInflater.inflate(R.layout.todo_item,parent,false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
          holder.bind()
        }

        override fun getItemCount(): Int = sizeList

    }

    companion object {
        fun newInstance() = TodoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}