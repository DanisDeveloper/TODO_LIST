package com.danis.android.todo_list.domain.TODO

import com.danis.android.todo_list.domain.Repository

class UpdateTODOItemUseCase(private val repository: Repository) {
    suspend fun updateTODOItem(caseTODO: CaseTODO){
        repository.updateTODOItem(caseTODO)
    }
}