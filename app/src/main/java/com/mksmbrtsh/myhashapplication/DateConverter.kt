package com.mksmbrtsh.myhashapplication

import androidx.room.TypeConverter
import java.util.*
/*
Date in DB
 */
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return timestamp.let { Date(it) }
    }
    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.getTime()
    }
}
