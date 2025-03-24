package com.example.myjournal.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = item.route == currentRoute
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = { onItemClick(item) }
            )
        }
    }
}
