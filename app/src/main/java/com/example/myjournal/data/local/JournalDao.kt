package com.example.myjournal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Query("SELECT * FROM journals ORDER BY date DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journals WHERE id = :id")
    suspend fun getJournalById(id: Int): JournalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Update
    suspend fun updateJournal(journal: JournalEntity)
}
