package com.mksmbrtsh.myhashapplication

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Message::class), version = 1)
@TypeConverters(DateConverter::class)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}