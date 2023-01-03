package com.danis.android.todo_list.domain.TODO

import androidx.lifecycle.LiveData
import com.danis.android.todo_list.domain.Repository
import java.util.*

class GetTODOListUseCase(private val repository: Repository) {
    suspend fun getTODOList(date: Long): List<CaseTODO> {
        return repository.getTODOList(date)
    }
}