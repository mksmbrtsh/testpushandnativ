package com.mksmbrtsh.myhashapplication
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
data class Message(
    @ColumnInfo(name = "input") val input: String,// input string for calc
    @ColumnInfo(name = "hash") val hash: String,// result calc
    @TypeConverters(DateConverter::class)
    @ColumnInfo(name = "date") val date: Date// datetime calc
) {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}