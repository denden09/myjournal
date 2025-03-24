package com.example.myjournal.data.local.repository

import com.example.myjournal.data.local.JournalDao
import com.example.myjournal.data.local.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {

    fun getAllJournals(): Flow<List<JournalEntity>> = journalDao.getAllJournals()

    suspend fun getJournalById(id: Int): JournalEntity? = journalDao.getJournalById(id)

    suspend fun insertJournal(journal: JournalEntity) = journalDao.insertJournal(journal)

    suspend fun deleteJournal(journal: JournalEntity) = journalDao.deleteJournal(journal)

    suspend fun updateJournal(journal: JournalEntity) = journalDao.updateJournal(journal)
}
