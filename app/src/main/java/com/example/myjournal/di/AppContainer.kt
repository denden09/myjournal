package com.example.myjournal.di

import android.content.Context
import androidx.room.Room
import com.example.myjournal.data.local.AppDatabase
import com.example.myjournal.data.local.repository.JournalRepository

// Singleton Dependency Container
class AppContainer(context: Context) {

    // Singleton instance of the Room database
    val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "journal_db"
    ).build()

    // Dao
    val journalDao = database.journalDao()

    // Repository
    val journalRepository = JournalRepository(journalDao)
}
