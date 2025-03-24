package com.example.myjournal.ui.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myjournal.data.local.repository.JournalRepository

class JournalViewModelFactory(
    private val repository: JournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            return JournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
