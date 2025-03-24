package com.example.myjournal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val date: String,
    val imageUri: String? = null,
    val location: String? = null
)
