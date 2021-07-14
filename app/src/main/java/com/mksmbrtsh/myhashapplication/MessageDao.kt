package com.mksmbrtsh.myhashapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM message ORDER BY date DESC ")
    fun getAll(): MutableList<Message>
    @Query("SELECT * FROM message ORDER BY date DESC LIMIT 1")
    fun getLast(): Message
    @Insert
    fun insertAll(vararg msgs: Message)
    @Delete
    fun delete(msg: Message)
}
