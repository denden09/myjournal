package com.example.myjournal.ui.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjournal.model.Journal
import com.example.myjournal.model.toInsertEntity
import com.example.myjournal.model.toJournal
import com.example.myjournal.model.toUpdateEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.example.myjournal.data.local.repository.JournalRepository


class JournalViewModel(
    private val repository: JournalRepository
) : ViewModel() {

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals.asStateFlow()

    init {
        getAllJournals()
    }

    private fun getAllJournals() {
        viewModelScope.launch {
            repository.getAllJournals().collect { entityList ->
                _journals.value = entityList.map { it.toJournal() }
            }
        }
    }

    // ✅ Pakai toInsertEntity() buat tambah data baru
    fun addJournal(journal: Journal) {
        viewModelScope.launch {
            repository.insertJournal(journal.toInsertEntity())
            getAllJournals()
        }
    }

    // ✅ Pakai toUpdateEntity() buat edit data lama
    fun updateJournal(journal: Journal) {
        viewModelScope.launch {
            if (journal.id != null) {
                repository.updateJournal(journal.toUpdateEntity())
                getAllJournals() // Tambahin ini biar StateFlow ke-refresh!
            } else {
                Log.e("JournalUpdate", "Journal ID is null. Update failed!")
            }
        }
    }



    fun deleteJournal(journal: Journal) {
        viewModelScope.launch {
            repository.deleteJournal(journal.toUpdateEntity())
            getAllJournals()
        }
    }



    suspend fun getJournalById(id: Int): Journal? {
        return repository.getJournalById(id)?.toJournal()
    }
}
