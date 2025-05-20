package com.example.myjournal.ui.journey

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myjournal.model.Journal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    journal: Journal,
    onUpdate: (title: String, content: String) -> Unit,
    onBack: () -> Unit, // Tambah fungsi back
    viewModel: JournalViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf(journal.title) }
    var content by remember { mutableStateOf(journal.content) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Journal") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (title.isBlank() || content.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Title & Content can't be empty!")
                            }
                        } else {
                            val updatedJournal = journal.copy(
                                title = title,
                                content = content
                            )
                            try {
                                viewModel.updateJournal(updatedJournal)
                                onUpdate(title, content)

                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Journal updated!")
                                }

                                // Optional: Kembali setelah update
                                onBack()
                            } catch (e: Exception) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to update journal.")
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save Changes")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Kalau mau preview gambar/lokasi, tambahin di sini
        }
    }
}
