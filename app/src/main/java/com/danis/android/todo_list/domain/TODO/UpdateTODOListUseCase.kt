package com.danis.android.todo_list.domain.TODO

import com.danis.android.todo_list.domain.Repository

class UpdateTODOListUseCase(private val repository: Repository) {
    suspend fun updateTODOList(list: List<CaseTODO>){
        repository.updateTODOList(list)
    }
}