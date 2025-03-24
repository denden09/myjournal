package com.example.myjournal.ui.journey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myjournal.model.Journal
import com.example.myjournal.model.MoodEntry
import com.example.myjournal.ui.components.MoodTrackerChart
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clip

// Data class for Journal
data class Journal(
    val id: Int,
    val date: String,
    val title: String,
    val content: String
)

// JournalListItem Composable
@Composable
fun JournalListItem(
    journal: Journal,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = journal.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = journal.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = journal.content.take(50) + "...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// JourneyScreen Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneyScreen(
    navController: NavHostController,
    journals: List<Journal>,
    onEntryClick: (Journal) -> Unit,
    onNewEntry: () -> Unit,
    username: String = "Vinsen"
) {
    var showTooltip by remember { mutableStateOf(true) }

    // Auto dismiss tooltip after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000)
        showTooltip = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        // Layout utama untuk konten dan tombol
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Row untuk teks dan tombol Search serta Profile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Menyebar teks dan tombol
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kolom untuk teks "MyJournal" dan "Hi, Vinsen"
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "MyJournal",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 32.sp,
                            fontFamily = FontFamily.Cursive
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hi, $username 👋",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Kolom untuk tombol Search dan Profile
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            }

            // Konten utama
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    // Mood Hari Ini
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mood,
                            contentDescription = "Mood Icon",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Mood Today",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "😊 Happy",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onNewEntry()
                            showTooltip = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Journal Entry")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Recent Journal Entries",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                items(journals) { journal ->
                    JournalListItem(
                        journal = journal,
                        onClick = { onEntryClick(journal) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        MoodTrackerChart(
                            moodData = listOf(
                                MoodEntry("Mon", 2),
                                MoodEntry("Tue", 3),
                                MoodEntry("Wed", 1),
                                MoodEntry("Thu", 2),
                                MoodEntry("Fri", 3),
                                MoodEntry("Sat", 2),
                                MoodEntry("Sun", 3)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Floating Action Button di kanan bawah
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {

                FloatingActionButton(
                    onClick = {
                        onNewEntry()
                        showTooltip = false
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Entry",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
