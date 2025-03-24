package com.example.myjournal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myjournal.di.AppContainer
import com.example.myjournal.model.Journal
import com.example.myjournal.navigation.NavGraph
import com.example.myjournal.ui.components.BottomNavBar
import com.example.myjournal.ui.components.BottomNavItem
import com.example.myjournal.ui.journey.JournalViewModel
import com.example.myjournal.ui.journey.JournalViewModelFactory
import com.example.myjournal.ui.theme.MyjournalTheme
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize AppContainer (manual DI without Hilt)
        appContainer = AppContainer(applicationContext)

        val repository = appContainer.journalRepository

        // ✅ Initialize ViewModel with Factory
        val journalViewModel = ViewModelProvider(
            this,
            JournalViewModelFactory(repository)
        )[JournalViewModel::class.java]

        setContent {
            MyjournalTheme {

                // ✅ Get window size class for responsive UI
                val windowSizeClass = calculateWindowSizeClass(this)

                val journals by journalViewModel.journals.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val bottomNavItems = listOf(
                        BottomNavItem("Journey", Icons.Default.Home, "journey"),
                        BottomNavItem("Calendar", Icons.Default.CalendarToday, "calendar"),
                        BottomNavItem("Media", Icons.Default.Image, "media"),
                        BottomNavItem("Atlas", Icons.Default.LocationOn, "atlas")
                    )

                    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

                    Scaffold(
                        bottomBar = {
                            // ✅ BottomBar SELALU tampil jika currentRoute sesuai (tanpa cek windowSizeClass)
                            if (showBottomBar) {
                                BottomNavBar(
                                    items = bottomNavItems,
                                    currentRoute = currentRoute ?: "journey",
                                    onItemClick = { item ->
                                        if (item.route != currentRoute) {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->

                        // ✅ Pass windowSizeClass ke NavGraph (tetap ada jika butuh di NavGraph)
                        NavGraph(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues),
                            journals = journals,
                            onAddJournal = { title, content, imageUri, location ->
                                val newJournal = Journal(
                                    id = generateRandomId(),
                                    title = title,
                                    content = content,
                                    date = getCurrentDate(),
                                    moodLevel = 3,
                                    imageUri = imageUri?.toString(),
                                    location = location?.toString()
                                )
                                journalViewModel.addJournal(newJournal)
                            },
                            onDeleteJournal = { journal ->
                                journalViewModel.deleteJournal(journal)
                            },
                            onUpdateJournal = { journal, updatedTitle, updatedContent ->
                                val updatedJournal = journal.copy(
                                    title = updatedTitle,
                                    content = updatedContent
                                )
                                journalViewModel.updateJournal(updatedJournal)
                            },
                            journalViewModel = journalViewModel,
                            windowSizeClass = windowSizeClass // ✅ Responsiveness ready
                        )
                    }
                }
            }
        }
    }

    private fun generateRandomId(): Int {
        return UUID.randomUUID().hashCode()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        return LocalDate.now().toString()
    }
}
