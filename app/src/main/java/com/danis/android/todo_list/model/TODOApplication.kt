package com.danis.android.todo_list.model

import android.app.Application
import android.content.Context
import java.util.*

class TODOApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.inizialize(this)
    }
}

