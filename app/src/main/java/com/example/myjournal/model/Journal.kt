package com.example.myjournal.model

data class Journal(
    val id: Int? = 0,
    val title: String,
    val content: String,
    val date: String,
    val imageUri: String?,
    val location: String?,
    val moodLevel: Int,

    // Menggunakan var untuk latitude dan longitude
    var latitude: Double? = null,
    var longitude: Double? = null
) {
    // Method untuk parsing lokasi (jika ada)
    fun parseLocation(): Boolean {
        if (!location.isNullOrEmpty()) {
            val parts = location.split(",")
            return if (parts.size == 2) {
                latitude = parts[0].toDoubleOrNull()
                longitude = parts[1].toDoubleOrNull()
                latitude != null && longitude != null
            } else {
                false
            }
        }
        return false
    }
}
