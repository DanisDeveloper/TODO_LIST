package com.danis.android.todo_list.presentation.fragments

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.danis.android.todo_list.AlarmReceiver
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.FragmentTodoItemBottomSheetBinding
import com.danis.android.todo_list.domain.TODO.CaseTODO
import com.danis.android.todo_list.presentation.viewmodel.TODOViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class TODOItemBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTodoItemBottomSheetBinding? = null
    private val binding: FragmentTodoItemBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoItemDialogBinding == null")

    private lateinit var inCaseTODO: CaseTODO
    private lateinit var outCaseTODO: CaseTODO

    var onDeleteClickListener: ((CaseTODO) -> Unit)? = null
    var onSaveClickListener: ((CaseTODO) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inCaseTODO = requireArguments().getSerializable(CASE_TODO) as CaseTODO
        outCaseTODO = inCaseTODO.copy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoItemBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkBox.isChecked = outCaseTODO.isSolved
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            outCaseTODO.isSolved = isChecked
        }
        binding.taskEditText.setText(outCaseTODO.todo)
        binding.taskEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                outCaseTODO.todo = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.dateTextView.text = getFormattedDate(outCaseTODO.date)
        binding.notificationTextView.text = getFormattedTime(outCaseTODO.notificationTime)
        binding.chipCalendar.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.date_dialog_task))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(
                requireActivity().supportFragmentManager,
                DATE_PICKER_DIALOG_TAG
            )
            datePicker.addOnPositiveButtonClickListener {
                val itDate = getDate(it).time
                if(outCaseTODO.notificationTime !=null){
                    val notificationTime = outCaseTODO.notificationTime!! - getDate(outCaseTODO.date).time
                    outCaseTODO.notificationTime = notificationTime + itDate
                }
                outCaseTODO.date = itDate
                binding.dateTextView.text = getFormattedDate(it)
            }
        }
        binding.chipNotification.setOnClickListener {
            if(outCaseTODO.notificationTime == null){
                showTimePicker(outCaseTODO)
            }
            else{
                outCaseTODO.notificationTime = null
                binding.notificationTextView.text = ""
            }
        }
        binding.chipDelete.setOnClickListener {
            onDeleteClickListener?.invoke(inCaseTODO)
            dismiss()
        }
        binding.chipCancel.setOnClickListener {
            dismiss()
        }
        binding.chipSave.setOnClickListener {
            onSaveClickListener?.invoke(outCaseTODO)
            dismiss()
        }
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.priority,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = spinnerAdapter
        binding.prioritySpinner.setSelection(0, false)
        binding.prioritySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val priority = NUMBER_OF_PRIORITIES - position
                    outCaseTODO.priority = priority
                    setBackgroundSpinnerByPriority(priority)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        setBackgroundSpinnerByPriority(outCaseTODO.priority)
    }

    private fun setBackgroundSpinnerByPriority(priority: Int) {
        when (priority) {
            NO_PRIORITY -> binding.prioritySpinner.setBackgroundResource(R.drawable.ic_priority)
            LOW_PRIORITY -> binding.prioritySpinner.setBackgroundResource(R.drawable.ic_priority_low)
            MEDIUM_PRIORITY -> binding.prioritySpinner.setBackgroundResource(R.drawable.ic_priority_medium)
            HIGH_PRIORITY -> binding.prioritySpinner.setBackgroundResource(R.drawable.ic_priority_high)
            else -> binding.prioritySpinner.setBackgroundResource(R.drawable.ic_priority)
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFormattedDate(date: Long?): String {
        return if (date != null) {
            val formatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            formatter.format(Date(date))
        } else ""
    }
    private fun getFormattedTime(date: Long?): String {
        return if (date != null) {
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            formatter.format(Date(date))
        } else ""
    }
    private fun getDate(date: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(date)
        calendar.get(Calendar.DATE)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return GregorianCalendar(year, month, day, 0, 0, 0).time
    }


    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showTimePicker(case: CaseTODO) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(getString(R.string.time_picker))
            .build()
        picker.show(requireActivity().supportFragmentManager, TIME_PICKER_TAG)

        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = Date(case.date)
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            case.notificationTime = calendar.time.time
            if(case.notificationId == null) case.notificationId = (Math.random()* MAX_RANDOM_VALUE).toInt()
            binding.notificationTextView.text = getFormattedTime(case.notificationTime)
        }
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    companion object {
        private const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
        private const val CASE_TODO = "CASE_TODO"
        private const val DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG"
        private const val NUMBER_OF_PRIORITIES = 3
        private const val NO_PRIORITY = 0
        private const val LOW_PRIORITY = 1
        private const val MEDIUM_PRIORITY = 2
        private const val HIGH_PRIORITY = 3
        private const val MAX_RANDOM_VALUE = 10000000

        fun newInstance(caseTODO: CaseTODO) = TODOItemBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CASE_TODO, caseTODO)
            }
        }
    }
}