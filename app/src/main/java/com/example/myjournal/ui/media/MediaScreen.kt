package com.example.myjournal.ui.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myjournal.R
import com.example.myjournal.model.Journal

// === DATA CLASS UNTUK MEDIA ITEM ===
data class MediaItem(
    val journalId: Int?,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    navController: NavHostController,
    journals: List<Journal>
) {
    // Font Pacifico untuk Title
    val pacifico = FontFamily(Font(R.font.pacifico))

    // Filter journals yang punya gambar (imageUri tidak null)
    val mediaItems: List<MediaItem> = journals.mapNotNull { journal ->
        journal.imageUri?.let { uri ->
            MediaItem(
                journalId = journal.id,
                imageUrl = uri
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Media",
                        style = TextStyle(
                            fontFamily = pacifico,
                            fontWeight = FontWeight.Normal, // Sesuaikan dengan berat font di CalendarScreen
                            fontSize = 32.sp, // Sesuaikan ukuran font
                            color = MaterialTheme.colorScheme.primary // Sesuaikan dengan warna yang digunakan di CalendarScreen
                        )
                    )
                },

            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (mediaItems.isEmpty()) {
                EmptyMediaView()
            } else {
                AnimatedVisibility(
                    visible = mediaItems.isNotEmpty(),
                    enter = fadeIn()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(mediaItems) { mediaItem ->
                            MediaItemCard(
                                mediaItem = mediaItem,
                                onClick = {
                                    navController.navigate("journal_detail/${mediaItem.journalId}") {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyMediaView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ImageNotSupported,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Belum ada gambar yang diunggah.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun MediaItemCard(
    mediaItem: MediaItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = mediaItem.imageUrl,
            contentDescription = "Journal Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
