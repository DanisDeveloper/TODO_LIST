package com.danis.android.todo_list.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.AlarmReceiver
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.getDate
import com.danis.android.todo_list.viewModel.TODOViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG"
private const val DATE_PICKER_DIALOG_REQUEST_CODE = 0
private const val CHOICE_DIALOG_TAG = "CHOICE_DIALOG_TAG"
private const val CHOICE_DIALOG_REQUEST_CODE = 1
private const val CHANNEL_ID = "CHANNEL_ID"
private const val CONTENT_TEXT_KEY = "CONTENT_TEXT_KEY"
private const val PICKER_TAG = "PICKER_TAG"
private const val CASE_ID = "CASE_ID"


class TodoFragment : Fragment(),DatePickerFragment.Callback,ChoiceDialogFragment.CallBack {
    private lateinit var binding:FragmentTodoBinding
    private  var adapter = Adapter(emptyList())
    private var TODOList:List<CaseTODO> = emptyList()
    private val todoViewModel:TODOViewModel by lazy {
        ViewModelProvider(this).get(TODOViewModel::class.java)
    }
    private var currentCase  = CaseTODO()
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel.loadTODOList(todoViewModel.currentDate)
        createNotificationChannel()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater,container,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setItemViewCacheSize(100)
        val simpleCallback = object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition
                Collections.swap(TODOList,startPosition,endPosition)
                TODOList[startPosition].position = startPosition
                TODOList[endPosition].position = endPosition
                adapter.notifyItemMoved(startPosition,endPosition)
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
        setDateTextView(todoViewModel.currentDate)
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
            if(TODOList.size<100)
                todoViewModel.onClickAddButton(todoViewModel.currentDate,TODOList.size)
            else
                Snackbar.make(binding.root,R.string.limit,Snackbar.LENGTH_SHORT).show()
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
            if(case.notificationTime!=null){
                if(case.notificationTime!! < Date()){
                    binding.notificationImageView.setColorFilter(context?.getResources()?.getColor(R.color.hint)!!)
                    cancelAlarm(case)
                }
                else
                    binding.notificationImageView.setColorFilter(context?.getResources()?.getColor(R.color.main_application_color)!!)
            }
            else
                binding.notificationImageView.setColorFilter(context?.getResources()?.getColor(R.color.hint)!!)

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
            binding.deleteImageView.setOnClickListener {
                activity?.let {
                    ChoiceDialogFragment.newInstance().apply {
                        setTargetFragment(this@TodoFragment,CHOICE_DIALOG_REQUEST_CODE)
                        show(it.supportFragmentManager,CHOICE_DIALOG_TAG)
                    }
                }
                currentCase = case
                todoViewModel.saveTODOList(TODOList)
            }
            binding.notificationImageView.setOnClickListener {
                if(case.notificationTime==null){
                    binding.notificationImageView.setColorFilter(context?.getResources()?.getColor(R.color.main_application_color)!!)
                    showTimePicker(case)
                }
                else{
                    binding.notificationImageView.setColorFilter(context?.getResources()?.getColor(R.color.hint)!!)
                    cancelAlarm(case)
                }

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
        todoViewModel.currentDate = date
        todoViewModel.loadTODOList(date)
        setDateTextView(todoViewModel.currentDate)
    }

    fun setDateTextView(date:Date){
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val string_date = formatter.format(date)
        binding.dateTextView.text = string_date
    }

    override fun onButtonClick(choice: Boolean) {
        if(choice){
            todoViewModel.deleteTodo(currentCase)
            for(elem in currentCase.position until TODOList.size){
                TODOList[elem].position--
            }
            todoViewModel.saveTODOList(TODOList)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name:CharSequence = "MyNotificationChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID,name,importance)
            channel.description = description
            val notificationManager = activity?.getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun showTimePicker(case: CaseTODO) {
        var picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("TIME PICKER")
            .build()
        picker.show(requireFragmentManager(),PICKER_TAG)

        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = case.date
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            case.notificationTime = calendar.time
            setAlarm(case)
        }
    }
    private fun setAlarm(case: CaseTODO) {
        alarmManager = activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(CONTENT_TEXT_KEY,case.todo)
        }
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,case.notificationTime?.time!!, pendingIntent)
    }
    private fun cancelAlarm(case: CaseTODO) {
        alarmManager = activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,AlarmReceiver::class.java).apply {
            putExtra(CONTENT_TEXT_KEY,case.todo)
        }
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        case.notificationTime = null
    }

    companion object {
        fun newInstance() = TodoFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}