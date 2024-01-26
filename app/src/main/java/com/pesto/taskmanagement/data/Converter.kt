package com.pesto.taskmanagement.data

import androidx.room.TypeConverter
import com.pesto.taskmanagement.data.models.Status

class Converter {

    @TypeConverter
    fun fromPriority(status: Status): String {
        return status.name
    }

    @TypeConverter
    fun toPriority(priority: String): Status {
        return Status.valueOf(priority)
    }
}