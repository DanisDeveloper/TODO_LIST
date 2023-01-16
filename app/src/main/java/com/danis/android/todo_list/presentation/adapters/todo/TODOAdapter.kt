package com.danis.android.todo_list.presentation.adapters.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.danis.android.todo_list.databinding.TodoItemBinding
import com.danis.android.todo_list.domain.TODO.CaseTODO

class TODOAdapter(TODOList: List<CaseTODO>) : RecyclerView.Adapter<TODOViewHolder>() {
    var TODOList: List<CaseTODO> = TODOList
        set(value) {
            val callback = TODODiffCallback(TODOList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }
    var onItemClickListener: ((CaseTODO) -> Unit)? = null
    var onItemLongClickListener: ((CaseTODO) -> Unit)? = null

    //private var countOnBind = 0
    //private var countOnCreate = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TODOViewHolder {
        //Log.d("TAG","onCreate ${countOnCreate++}")
        val binding = TodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TODOViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TODOViewHolder, position: Int) {
        //Log.d("TAG","onBind ${countOnBind++}")
        val caseTODO = TODOList[position]
        holder.bind(caseTODO)
        holder.checkBackground(caseTODO.isSolved)

        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(caseTODO)
        }
        holder.binding.root.setOnLongClickListener {
            onItemLongClickListener?.invoke(caseTODO)
            true
        }
    }

    override fun getItemCount(): Int = TODOList.size



}

