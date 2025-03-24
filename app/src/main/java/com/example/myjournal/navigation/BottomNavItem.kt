package com.example.myjournal.navigation

import androidx.compose.ui.graphics.vector.ImageVector

// ✅ Ini adalah data class buat item navigasi bawah (Bottom Navigation)
data class BottomNavItem(
    val title: String,          // Ini yang tadi error 'title' gak ditemukan!
    val icon: ImageVector,      // Ikon untuk navigasi
    val route: String           // Route buat navigasi di NavController
)
