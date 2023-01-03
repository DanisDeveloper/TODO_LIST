package com.danis.android.todo_list.domain.TODO

import com.danis.android.todo_list.domain.Repository

class InsertTODOItemUseCase(private val repository: Repository) {
    suspend fun insertTODOItem(caseTODO: CaseTODO){
        repository.insertTODOItem(caseTODO)
    }
}