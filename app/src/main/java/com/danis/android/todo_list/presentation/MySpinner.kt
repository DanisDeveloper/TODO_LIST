package com.danis.android.todo_list.presentation

import android.content.Context
import android.util.AttributeSet


class MySpinner(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatSpinner(context!!, attrs) {
    var listener: OnItemSelectedListener? = null
    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (position == selectedItemPosition) {
            listener!!.onItemSelected(null, null, position, 0)
        }
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }
}