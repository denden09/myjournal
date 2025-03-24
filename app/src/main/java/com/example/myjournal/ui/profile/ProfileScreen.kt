package com.example.myjournal.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myjournal.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Logout


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    // Dummy data sementara
    val username = "Nama Pengguna"
    val email = "email@example.com"
    val bio = "Ini adalah bio singkat pengguna."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto profil dummy
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground), // ganti sama gambar profil user nanti
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama pengguna
            Text(text = username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Bio
            Text(text = bio, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol edit profile
            Button(
                onClick = {
                    navController.navigate("edit_profile") },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol logout
            OutlinedButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("journey") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Logout")
            }
        }
    }
}
