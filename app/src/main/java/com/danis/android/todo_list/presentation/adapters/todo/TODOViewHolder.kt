package com.danis.android.todo_list.presentation.adapters.todo

import android.content.res.ColorStateList
import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.R
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.domain.TODO.CaseTODO

class TODOViewHolder(
    val binding: TodoItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(caseTODO: CaseTODO) {
        binding.taskEditText.text = caseTODO.todo
        checkForStrike(caseTODO.isSolved)
        checkBackground(caseTODO.isSolved)
        checkPriority(caseTODO.priority)
    }

    private fun checkForStrike(isChecked: Boolean) {
        if (isChecked) strike(binding.taskEditText)
        else unstrike(binding.taskEditText)
    }

    private fun strike(textView: TextView) {
        val STRIKE_THROUGH_SPAN = StrikethroughSpan()
        textView.setText(textView.text, TextView.BufferType.SPANNABLE)
        val spannable: Spannable = binding.taskEditText.text as Spannable
        spannable.setSpan(
            STRIKE_THROUGH_SPAN,
            0,
            binding.taskEditText.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun unstrike(textView: TextView) {
        binding.taskEditText.setText(textView.text, TextView.BufferType.NORMAL)
    }

    fun checkBackground(isChecked: Boolean) {
        if (isChecked)
            binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    binding.constraintLayout.context,
                    R.color.item_disabled_background
                )
            )
        else
            binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    binding.constraintLayout.context,
                    R.color.item_enabled_background
                )
            )
    }

    fun checkPriority(priority: Int) {
        when (priority) {
            NO_PRIORITY -> binding.priorityImageView.visibility = View.GONE
            LOW_PRIORITY -> {
                binding.priorityImageView.visibility = View.VISIBLE
                binding.priorityImageView.setBackgroundResource(R.drawable.ic_priority_low)
            }
            MEDIUM_PRIORITY -> {
                binding.priorityImageView.visibility = View.VISIBLE
                binding.priorityImageView.setBackgroundResource(R.drawable.ic_priority_medium)
            }
            HIGH_PRIORITY -> {
                binding.priorityImageView.visibility = View.VISIBLE
                binding.priorityImageView.setBackgroundResource(R.drawable.ic_priority_high)
            }
            else -> binding.priorityImageView.visibility = View.GONE
        }
    }

    companion object {
        private const val NO_PRIORITY = 0
        private const val LOW_PRIORITY = 1
        private const val MEDIUM_PRIORITY = 2
        private const val HIGH_PRIORITY = 3
    }
}