package com.danis.android.todo_list.domain.TODO

import com.danis.android.todo_list.domain.Repository

class DeleteTODOItemUseCase(private val repository: Repository) {
    suspend fun deleteTODOItem(caseTODO: CaseTODO){
        repository.deleteTODOItem(caseTODO)
    }
}