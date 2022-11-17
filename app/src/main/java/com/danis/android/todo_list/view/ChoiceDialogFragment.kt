package com.danis.android.todo_list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.danis.android.todo_list.databinding.ChoiceDialogFragmentBinding

class ChoiceDialogFragment:DialogFragment() {
    interface CallBack{
        fun onButtonClick(choice:Boolean)
    }
    private lateinit var binding:ChoiceDialogFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChoiceDialogFragmentBinding.inflate(inflater,container,false)
        binding.apply {
            yesButton.setOnClickListener {
                targetFragment?.let {
                    (it as CallBack).onButtonClick(true)
                }
                dismiss()
            }
            noButton.setOnClickListener {
                targetFragment?.let {
                    (it as CallBack).onButtonClick(false)
                }
                dismiss()
            }
        }
        return binding.root
    }

    companion object{
        fun newInstance() = ChoiceDialogFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}