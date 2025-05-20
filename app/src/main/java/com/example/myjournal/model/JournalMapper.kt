package com.example.myjournal.model

import com.example.myjournal.data.local.JournalEntity
import com.example.myjournal.model.Journal

// ✅ Entity ke Domain Model (Dari Database ke UI)
fun JournalEntity.toJournal(): Journal {
    return Journal(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        moodLevel = 3,
        imageUri = this.imageUri,
        location = this.location
    )
}

// ✅ Domain Model ke Entity untuk INSERT (New Journal)
// id = 0, biar Room autoGenerate id-nya
fun Journal.toInsertEntity(): JournalEntity {
    return JournalEntity(
        id = 0, // Room bakal generate id baru
        title = this.title,
        content = this.content,
        date = this.date,
        imageUri = this.imageUri,
        location = this.location
    )
}

fun Journal.toUpdateEntity(): JournalEntity {
    requireNotNull(this.id) { "ID nggak boleh null buat update!" }

    return JournalEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        date = this.date,
        imageUri = this.imageUri,
        location = this.location
    )
}

