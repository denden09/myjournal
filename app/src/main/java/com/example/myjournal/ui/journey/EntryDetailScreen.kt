package com.example.myjournal.ui.journey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myjournal.model.Journal
import com.example.myjournal.R // Sesuaikan resource!

@Composable
fun EntryDetailScreen(
    journal: Journal,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                onClick = {
                    // Optional: Konfirmasi sebelum hapus
                    onDelete()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun MoodIndicator(moodLevel: Int) {
    val (color, emoji) = when (moodLevel) {
        1 -> Color.Red to "😢"
        2 -> Color(0xFFFFA000) to "😐"
        3 -> Color.Green to "😊"
        else -> Color.Gray to "❓"
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
