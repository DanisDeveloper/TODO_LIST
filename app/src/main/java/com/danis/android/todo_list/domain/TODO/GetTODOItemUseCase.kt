package com.danis.android.todo_list.domain.TODO

import com.danis.android.todo_list.domain.Repository

class GetTODOItemUseCase(private val repository: Repository) {
    suspend fun getTODOItem(id:String): CaseTODO {
        return repository.getTODOItem(id)
    }
}