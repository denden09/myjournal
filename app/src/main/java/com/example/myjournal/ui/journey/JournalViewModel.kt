package com.example.myjournal.ui.journey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myjournal.data.local.repository.JournalRepository
import com.example.myjournal.model.Journal
import com.example.myjournal.model.toInsertEntity
import com.example.myjournal.model.toJournal
import com.example.myjournal.model.toUpdateEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class JournalViewModel(
    private val repository: JournalRepository
) : ViewModel() {

    private val _journals = MutableStateFlow<List<Journal>>(emptyList())
    val journals: StateFlow<List<Journal>> = _journals.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val journalCollection = firestore.collection("journals")

    init {
        getAllJournals()
        observeFirebaseChanges()
    }

    private fun observeFirebaseChanges() {
        journalCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FirebaseSnapshot", "Listen failed", error)
                return@addSnapshotListener
            }

            val firebaseJournals = snapshot?.documents?.mapNotNull { doc ->
                val id = doc.id.toIntOrNull()
                val title = doc.getString("title") ?: return@mapNotNull null
                val content = doc.getString("content") ?: ""
                val date = doc.getString("date") ?: ""
                val imageUri = doc.getString("imageUri")
                val locationStr = doc.getString("location")
                val moodLevel = doc.getLong("moodLevel")?.toInt() ?: 0

                val (lat, lng) = parseLatLng(locationStr)

                id?.let {
                    Journal(
                        id = it,
                        title = title,
                        content = content,
                        date = date,
                        imageUri = imageUri,
                        location = locationStr,
                        moodLevel = moodLevel,
                        latitude = lat,
                        longitude = lng
                    )
                }
            } ?: emptyList()

            viewModelScope.launch {
                repository.clearAllJournals()
                repository.insertJournals(firebaseJournals.map { it.toInsertEntity() })
            }
        }
    }

    private fun getAllJournals() {
        viewModelScope.launch {
            repository.getAllJournals().collect { entityList ->
                _journals.value = entityList.map { it.toJournal() }
            }
        }
    }

    fun addJournal(journal: Journal) {
        viewModelScope.launch {
            repository.insertJournal(journal.toInsertEntity())
            getAllJournals() // Memperbarui daftar jurnal setelah menambah jurnal baru
        }
        addJournalInFirebase(journal)
    }

    fun updateJournal(journal: Journal) {
        viewModelScope.launch {
            if (journal.id != null) {
                repository.updateJournal(journal.toUpdateEntity())
                getAllJournals() // Memperbarui daftar jurnal setelah mengedit jurnal
            } else {
                Log.e("JournalUpdate", "Journal ID is null. Update failed!")
            }
        }
        updateJournalInFirebase(journal)
    }

    fun deleteJournal(journal: Journal) {
        viewModelScope.launch {
            if (journal.id != null) {
                // First delete the journal locally
                repository.deleteJournal(journal.toUpdateEntity())

                // Then delete it from Firebase
                deleteJournalInFirebase(journal.id)

                // Refresh the local list after deletion
                getAllJournals()
            } else {
                Log.e("DeleteJournal", "Journal ID is null. Cannot delete.")
            }
        }
    }


    suspend fun getJournalById(id: Int): Journal? {
        return repository.getJournalById(id)?.toJournal()
    }

    // === 🔥 FIREBASE METHODS ===

    private fun addJournalInFirebase(journal: Journal) {
        val id = journal.id.toString()
        val firebaseData = hashMapOf(
            "title" to journal.title,
            "content" to journal.content,
            "date" to journal.date,
            "imageUri" to journal.imageUri,
            "location" to journal.location,
            "moodLevel" to journal.moodLevel
        )

        journalCollection.document(id)
            .set(firebaseData)
            .addOnSuccessListener {
                Log.d("FirebaseAdd", "Journal added to Firestore")
            }
            .addOnFailureListener {
                Log.e("FirebaseAdd", "Failed to add journal to Firestore", it)
            }
    }

    private fun updateJournalInFirebase(journal: Journal) {
        val id = journal.id.toString()
        val firebaseData = hashMapOf(
            "title" to journal.title,
            "content" to journal.content,
            "date" to journal.date,
            "imageUri" to journal.imageUri,
            "location" to journal.location,
            "moodLevel" to journal.moodLevel
        )

        journalCollection.document(id)
            .set(firebaseData)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Journal updated in Firestore")
            }
            .addOnFailureListener {
                Log.e("FirebaseUpdate", "Failed to update journal in Firestore", it)
            }
    }

    private fun deleteJournalInFirebase(id: Int?) {
        if (id == null) {
            Log.e("FirebaseDelete", "ID is null. Cannot delete from Firestore.")
            return
        }

        journalCollection.document(id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d("FirebaseDelete", "Journal deleted from Firestore")
            }
            .addOnFailureListener {
                Log.e("FirebaseDelete", "Failed to delete journal from Firestore", it)
            }
    }

    // === 🔁 Helper ===

    private fun parseLatLng(locationStr: String?): Pair<Double?, Double?> {
        if (locationStr.isNullOrBlank()) return Pair(null, null)
        val parts = locationStr.split(",")
        return if (parts.size == 2) {
            val lat = parts[0].toDoubleOrNull()
            val lng = parts[1].toDoubleOrNull()
            Pair(lat, lng)
        } else {
            Pair(null, null)
        }
    }
}
