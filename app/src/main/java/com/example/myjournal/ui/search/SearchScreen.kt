package com.example.myjournal.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myjournal.model.Journal
import com.example.myjournal.data.local.AppDatabase
import com.example.myjournal.data.local.repository.JournalRepository
import com.example.myjournal.ui.journey.JournalViewModel
import com.example.myjournal.ui.journey.JournalViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController
) {
    val context = LocalContext.current

    // Manual init ViewModel tanpa Hilt
    val db = AppDatabase.getDatabase(context)
    val repository = JournalRepository(db.journalDao())
    val journalViewModel: JournalViewModel = viewModel(
        factory = JournalViewModelFactory(repository)
    )

    val journals by journalViewModel.journals.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredJournals = journals.filter { journal ->
        journal.title.contains(searchQuery, ignoreCase = true) ||
                journal.content.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Journals") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Search Results
            if (filteredJournals.isEmpty()) {
                Text("No results found", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredJournals) { journal ->
                        JournalListItem(
                            journal = journal,
                            onClick = {
                                navController.navigate("journal_detail/${journal.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalListItem(journal: Journal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(journal.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(journal.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}
