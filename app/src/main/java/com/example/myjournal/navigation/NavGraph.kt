package com.example.myjournal.navigation

import android.location.Location
import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myjournal.model.Journal
import com.example.myjournal.ui.auth.AuthViewModel
import com.example.myjournal.ui.auth.LoginScreen
import com.example.myjournal.ui.auth.SignUpScreen
import com.example.myjournal.ui.atlas.AtlasScreen
import com.example.myjournal.ui.calendar.CalendarScreen
import com.example.myjournal.ui.journey.*
import com.example.myjournal.ui.media.MediaScreen
import com.example.myjournal.ui.profile.EditProfileScreen
import com.example.myjournal.ui.profile.ProfileScreen
import com.example.myjournal.ui.search.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    journals: List<Journal>,
    onAddJournal: (String, String, Uri?, Location?) -> Unit,
    onDeleteJournal: (Journal) -> Unit,
    onUpdateJournal: (Journal, String, String) -> Unit,
    journalViewModel: JournalViewModel,
    windowSizeClass: WindowSizeClass
) {
    // 🔐 ViewModel untuk login status
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isUserLoggedIn()) "journey" else "login",
        modifier = modifier
    ) {

        /** AUTH SECTION **/
        composable("login") {
            LoginScreen(
                onLogin = {
                    navController.navigate("journey") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUp = {
                    navController.navigate("signup")
                },
                authViewModel = authViewModel
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("journey") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        /** JOURNEY SECTION **/
        composable("journey") {
            JourneyScreen(
                navController = navController,
                journals = journals,
                onEntryClick = { journal ->
                    navController.navigate("journal_detail/${journal.id}")
                },
                onNewEntry = {
                    navController.navigate("newentry")
                }
            )
        }

        composable("newentry") {
            NewEntryScreen(
                onSave = { title, content, imageUri, location ->
                    onAddJournal(title, content, imageUri, location)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "journal_detail/{journalId}",
            arguments = listOf(navArgument("journalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val journalId = backStackEntry.arguments?.getInt("journalId")
            val journal = journals.find { it.id == journalId }

            if (journal != null) {
                EntryDetailScreen(
                    journal = journal,
                    onEdit = {
                        navController.navigate("edit/${journal.id}")
                    },
                    onDelete = {
                        onDeleteJournal(journal)
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Journal not found")
            }
        }

        composable(
            route = "edit/{journalId}",
            arguments = listOf(navArgument("journalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val journalId = backStackEntry.arguments?.getInt("journalId")
            val journal = journals.find { it.id == journalId }

            if (journal != null) {
                EditEntryScreen(
                    journal = journal,
                    onUpdate = { updatedTitle, updatedContent ->
                        onUpdateJournal(journal, updatedTitle, updatedContent)
                        navController.popBackStack()
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                    viewModel = journalViewModel
                )
            } else {
                Text("Journal not found")
            }
        }

        /** EXTRA FEATURE SCREENS **/
        composable("calendar") {
            CalendarScreen(
                navController = navController,
                journals = journals,
                onNewEntry = {
                    navController.navigate("newentry")
                }
            )
        }

        composable("media") {
            MediaScreen(
                navController = navController,
                journals = journals
            )
        }

        composable("atlas") {
            AtlasScreen(journals = journals)
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }

        composable("edit_profile") {
            EditProfileScreen(navController = navController)
        }

        composable("search") {
            SearchScreen(navController = navController)
        }
    }
}
