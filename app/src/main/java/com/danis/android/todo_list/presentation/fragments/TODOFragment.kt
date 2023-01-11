package com.danis.android.todo_list.presentation.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.AlarmReceiver
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoBinding
import com.danis.android.todo_list.domain.TODO.CaseTODO
import com.danis.android.todo_list.presentation.adapters.todo.TODOAdapter
import com.danis.android.todo_list.presentation.viewmodel.TODOViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.*

class TODOFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding: FragmentTodoBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoBinding == null")

    private val todoViewModel: TODOViewModel by lazy {
        ViewModelProvider(this)[TODOViewModel::class.java]
    }

    private lateinit var todoAdapter: TODOAdapter
    private lateinit var snackBarUndo: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomSnackBar()
        setupRecyclerView()
        todoViewModel.dateLiveData.observe(viewLifecycleOwner) {
            todoViewModel.loadTODOList(it)
        }
        todoViewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.dateTextView.text = it
        }
        todoViewModel.todoListLiveData.observe(viewLifecycleOwner) {
            todoAdapter.TODOList = it
            if (it.isEmpty()) binding.taskNotFoundImageView.visibility = View.VISIBLE
            else binding.taskNotFoundImageView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        todoAdapter = TODOAdapter(todoViewModel.TODOList)
        binding.recyclerView.adapter = todoAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        setupItemTouchHelper()
        setupClickListeners()
    }

    private fun setupItemTouchHelper() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val caseTODO = todoAdapter.TODOList[viewHolder.adapterPosition]
                deleteTODOItemWithSnackBar(caseTODO)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun deleteTODOItemWithSnackBar(caseTODO: CaseTODO){
        todoViewModel.deleteTodo(caseTODO)
        cancelAlarm(caseTODO)
        snackBarUndo.setAction(R.string.undo){
            todoViewModel.insertTODOItem(caseTODO)
            if(caseTODO.notificationTime !=null)
                if(caseTODO.notificationTime!! >= Date().time)
                    setAlarm(caseTODO)
        }
        snackBarUndo.show()
    }

    private fun setCustomSnackBar(){
        snackBarUndo = Snackbar.make(
            binding.coordinatorLayout,
            R.string.snack_bar_delete_todo,
            Snackbar.LENGTH_LONG
        )
        snackBarUndo.setBackgroundTint(ContextCompat.getColor(
            requireActivity(),
            R.color.item_enabled_background
        ))
        snackBarUndo.setTextColor(ContextCompat.getColor(
            requireActivity(),
            R.color.text_color
        ))
        snackBarUndo.setActionTextColor(ContextCompat.getColor(
            requireActivity(),
            R.color.orange
        ))
        snackBarUndo.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    }

    private fun setupClickListeners() {
        todoAdapter.onItemClickListener = {
            val bottomSheet = TODOItemBottomSheetDialogFragment.newInstance(it)
            bottomSheet.onSaveClickListener = { caseTODO ->
                todoViewModel.updateTODOItem(caseTODO)
                if(caseTODO.notificationTime != null){
                    cancelAlarm(caseTODO)
                    setAlarm(caseTODO)
                }
                else{
                    cancelAlarm(caseTODO)
                }
            }
            bottomSheet.onDeleteClickListener = { caseTODO ->
                deleteTODOItemWithSnackBar(caseTODO)
            }
            bottomSheet.show(
                requireActivity().supportFragmentManager,
                BOTTOM_SHEET_DIALOG
            )
        }
        todoAdapter.onItemLongClickListener = {
            todoViewModel.changeEnabledState(it)
        }
    }

    private fun setAlarm(case: CaseTODO) {
        val alarmManager = activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(CONTENT_TEXT_KEY, case.todo)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            case.notificationId!!,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            case.notificationTime!!,
            pendingIntent
        )
    }

    private fun cancelAlarm(case: CaseTODO) {
        if(case.notificationId !=null) {
            val alarmManager =
                activity?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                case.notificationId!!,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.addButton.setOnClickListener {
            todoViewModel.onClickAddButton()
        }
        binding.calendarButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.date_dialog))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(requireActivity().supportFragmentManager, DATE_PICKER_DIALOG_TAG)
            datePicker.addOnPositiveButtonClickListener {
                todoViewModel.onClickCalendarButton(Date(it))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        todoViewModel.saveTODOList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CONTENT_TEXT_KEY = "CONTENT_TEXT_KEY"
        private const val DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG"
        private const val BOTTOM_SHEET_DIALOG = "BOTTOM_SHEET_DIALOG"

        fun newInstance() = TODOFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}