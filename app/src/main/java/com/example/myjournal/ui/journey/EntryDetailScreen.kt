// EntryDetailScreen.kt
package com.example.myjournal.ui.journey

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myjournal.model.Journal
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EntryDetailScreen(
    journal: Journal,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header: Date and Mood
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = journal.date,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )
                journal.location?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }

            // Mood Indicator
            MoodIndicator(moodLevel = journal.moodLevel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Section
        journal.imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Journal Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Title
        Text(
            text = journal.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Content
        Text(
            text = journal.content,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onEdit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Edit")
            }

            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this journal entry?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete() // Call onDelete from ViewModel
                    showDeleteDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun MoodIndicator(moodLevel: Int) {
    val (color, emoji) = when (moodLevel) {
        1 -> Color.Red to "😢"
        2 -> Color(0xFFFFA000) to "😐"
        3 -> Color.Green to "😊"
        else -> Color.Gray to "🤷"
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        Text(
            text = emoji,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEntryDetailScreen() {
    // Data Dummy untuk Preview
    val dummyJournal = Journal(
        id = 1,
        title = "My Journal Entry",
        date = "2025-05-01",
        content = "This is the content of my journal entry. It's a great day today!",
        moodLevel = 3,
        imageUri = "https://example.com/image.jpg", // Ganti dengan image URL yang valid
        location = "New York"
    )

    // Memanggil EntryDetailScreen dengan data dummy
    EntryDetailScreen(
        journal = dummyJournal,
        onEdit = { /* Edit logic */ },
        onDelete = { /* Delete logic */ }
    )
}
