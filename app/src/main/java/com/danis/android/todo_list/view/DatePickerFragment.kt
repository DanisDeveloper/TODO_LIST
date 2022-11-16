package com.danis.android.todo_list.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "ARG_DATE"

class DatePickerFragment:DialogFragment() {

    interface Callback{
        fun onDateSelected(date:Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val init_day = calendar.get(Calendar.DAY_OF_MONTH)
        val init_month = calendar.get(Calendar.MONTH)
        val init_year = calendar.get(Calendar.YEAR)
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val resultDate:Date = GregorianCalendar(year,month,dayOfMonth,0,0).time
            targetFragment?.let {
                (it as Callback).onDateSelected(resultDate)
            }
        }
        return DatePickerDialog(requireContext(),android.R.style.ThemeOverlay_Material_Dialog_Alert ,dateListener,init_year,init_month,init_day)
    }
    companion object{
        fun newInstance(date: Date) = DatePickerFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DATE,date)
            }
        }
    }
}