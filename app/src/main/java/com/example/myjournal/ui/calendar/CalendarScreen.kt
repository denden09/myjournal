package com.example.myjournal.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myjournal.R
import com.example.myjournal.model.Journal
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    journals: List<Journal> = listOf(),
    onNewEntry: () -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var currentMonth by remember { mutableStateOf(LocalDate.now()) }

    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val yearMonth = YearMonth.of(currentMonth.year, currentMonth.month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.withDayOfMonth(1).dayOfWeek.value % 7

    val dates = buildList {
        repeat(firstDayOfMonth) { add(null) }
        for (day in 1..daysInMonth) {
            add(LocalDate.of(currentMonth.year, currentMonth.month, day))
        }
    }

    val datesWithJournal = journals.mapNotNull {
        try {
            LocalDate.parse(it.date)
        } catch (e: Exception) {
            null
        }
    }

    val selectedJournals = journals.filter {
        try {
            LocalDate.parse(it.date) == selectedDate
        } catch (e: Exception) {
            false
        }
    }

    // ======= FONT PACIFICO =======
    val pacificoFont = FontFamily(Font(R.font.pacifico))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // ======= HEADER ========
        Text(
            text = "Calendar",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = pacificoFont,
                fontSize = 32.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ====== MONTH NAVIGATION ======
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = currentMonth.format(monthFormatter),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== DAY LABELS ======
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ===== GRID DATE ======
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxHeight(0.7f)
        ) {
            items(dates) { date ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = when {
                                date == selectedDate -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else -> Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .clickable {
                            selectedDate = if (date != null && datesWithJournal.contains(date)) {
                                date
                            } else {
                                null
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date?.dayOfMonth?.toString() ?: "",
                            color = if (date == selectedDate)
                                MaterialTheme.colorScheme.primary
                            else if (datesWithJournal.contains(date))
                                MaterialTheme.colorScheme.onBackground
                            else
                                Color.Gray,
                            fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal
                        )

                        if (datesWithJournal.contains(date)) {
                            Spacer(modifier = Modifier.height(4.dp))
                            DotIndicator(MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== BUTTON ADD JOURNAL ======
        Button(
            onClick = {
                onNewEntry()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Add New Journal")
        }
    }

    // ===== DETAIL JOURNAL DIALOG =====
    if (selectedDate != null && selectedJournals.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { selectedDate = null },
            confirmButton = {
                TextButton(onClick = { selectedDate = null }) {
                    Text("Close")
                }
            },
            title = {
                Text(
                    text = "Entries on ${selectedDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    selectedJournals.forEach { journal ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = journal.title,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = journal.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        )
    }
}

@Composable
fun DotIndicator(color: Color) {
    Canvas(modifier = Modifier.size(6.dp)) {
        drawCircle(color)
    }
}
