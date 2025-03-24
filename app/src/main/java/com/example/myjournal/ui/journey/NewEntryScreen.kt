package com.example.myjournal.ui.journey

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myjournal.R
import com.example.myjournal.utils.LocationUtils
import com.example.myjournal.utils.saveBitmapToCacheAndGetUri
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    onSave: (title: String, content: String, imageUri: Uri?, location: Location?) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf(TextFieldValue()) }
    var content by remember { mutableStateOf(TextFieldValue()) }

    val todayDate = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            selectedImageUri = saveBitmapToCacheAndGetUri(it, context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(todayDate) },
                actions = {
                    IconButton(onClick = {
                        if (title.text.isBlank() || content.text.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Title & Content can't be empty!")
                            }
                        } else {
                            onSave(
                                title.text.trim(),
                                content.text.trim(),
                                selectedImageUri,
                                selectedLocation
                            )
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Journal saved!")
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
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

            selectedImageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            selectedLocation?.let { loc ->
                Text(
                    text = "📍 Lat: ${loc.latitude}, Lon: ${loc.longitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    galleryLauncher.launch("image/*")
                }) {
                    Icon(Icons.Default.Image, contentDescription = "Pick from Gallery")
                }

                IconButton(onClick = {
                    cameraLauncher.launch(null)
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Take Photo")
                }

                IconButton(onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        val location = LocationUtils.getLastKnownLocation(context)
                        isLoading = false
                        if (location != null) {
                            selectedLocation = location
                            content = TextFieldValue(
                                "${content.text}\n\n📍 Location: ${location.latitude}, ${location.longitude}"
                            )
                            snackbarHostState.showSnackbar(
                                "Location added: ${location.latitude}, ${location.longitude}"
                            )
                        } else {
                            snackbarHostState.showSnackbar("Location not available. Check GPS!")
                        }
                    }
                }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Add Location")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewEntryScreen() {
    NewEntryScreen(
        onSave = { _, _, _, _ -> }
    )
}
