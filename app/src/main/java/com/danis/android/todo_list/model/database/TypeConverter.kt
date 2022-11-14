package com.danis.android.todo_list.model.database

import androidx.room.TypeConverter
import java.util.*

class TypeConverter {
    @TypeConverter
    fun fromDate(date: Date?):Long?{
        return date?.time
    }
    @TypeConverter
    fun toDate(ms:Long?):Date?{
        return ms?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromUUID(uuid:UUID?):String?{
        return uuid?.toString()
    }
    @TypeConverter
    fun toUUID(uuid:String?):UUID?{
        return UUID.fromString(uuid)
    }
}