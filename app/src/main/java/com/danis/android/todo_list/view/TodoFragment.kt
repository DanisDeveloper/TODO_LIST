package com.danis.android.todo_list.view

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.getDate
import com.danis.android.todo_list.viewModel.TODOViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

private const val DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG"
private const val DATE_PICKER_DIALOG_REQUEST_CODE = 0

class TodoFragment : Fragment(),DatePickerFragment.Callback {
    private lateinit var binding:FragmentTodoBinding
    private  var adapter = Adapter(emptyList())
    private var TODOList:List<CaseTODO> = emptyList()
    private val todoViewModel:TODOViewModel by lazy {
        ViewModelProvider(this).get(TODOViewModel::class.java)
    }
    private var currentDate:Date = getDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel.loadTODOList(currentDate)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater,container,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setItemViewCacheSize(100)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todoViewModel.todoListLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list->
            TODOList = list
            adapter = Adapter(TODOList)
            binding.recyclerView.adapter = adapter
            if(TODOList.isEmpty()) binding.taskNotFoundImageView.visibility = View.VISIBLE
            else binding.taskNotFoundImageView.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        binding.addButton.setOnClickListener {
            todoViewModel.saveTODOList(TODOList)
            todoViewModel.onClickAddButton(currentDate)
        }
        binding.calendarButton.setOnClickListener {
            todoViewModel.saveTODOList(TODOList)
            activity?.let { it ->
                DatePickerFragment.newInstance(getDate()).apply {
                    setTargetFragment(this@TodoFragment,DATE_PICKER_DIALOG_REQUEST_CODE)
                    show(it.supportFragmentManager,DATE_PICKER_DIALOG_TAG)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        todoViewModel.saveTODOList(TODOList)
    }

    private inner class Holder(view:View):RecyclerView.ViewHolder(view){
        var binding = TodoItemBinding.bind(view)
        fun bind(case:CaseTODO){
            val index = TODOList.indexOfFirst { it.id==case.id }
            binding.checkBox.isChecked = TODOList[index].isSolved
            binding.taskEditText.setText(TODOList[index].todo, TextView.BufferType.EDITABLE)
            checkForStrike(binding.checkBox.isChecked)
            binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                TODOList[index].isSolved = isChecked
                checkForStrike(isChecked)
            }
            binding.taskEditText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    TODOList[index].todo = s.toString()
                }
                override fun afterTextChanged(s: Editable?) {}

            })
            itemView.setOnClickListener {
                Snackbar.make(view!!,binding.taskEditText.text.toString(),Snackbar.LENGTH_SHORT).show()
            }
        }
       private fun checkForStrike(isChecked:Boolean){
           if(isChecked) strike(binding.taskEditText.text.toString())
           else unstrike(binding.taskEditText.text.toString())
       }
        private fun strike(str:String){
            val STRIKE_THROUGH_SPAN = StrikethroughSpan()
            binding.taskEditText.setText(str,TextView.BufferType.SPANNABLE)
            val spannable:Spannable = binding.taskEditText.text
            spannable.setSpan(STRIKE_THROUGH_SPAN,0,binding.taskEditText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        private fun unstrike(str:String){
            binding.taskEditText.setText(str,TextView.BufferType.NORMAL)
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

    override fun onDateSelected(date: Date) {
        currentDate = date
        todoViewModel.loadTODOList(date)
    }

    companion object {
        fun newInstance() = TodoFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }


}