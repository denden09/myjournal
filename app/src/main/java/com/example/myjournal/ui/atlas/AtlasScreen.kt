package com.example.myjournal.ui.atlas

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.myjournal.R
import com.example.myjournal.model.Journal
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.foundation.background


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtlasScreen(journals: List<Journal>) {
    val context = LocalContext.current

    // ✅ Gunakan Font Pacifico agar konsisten dengan CalendarScreen
    val pacificoFont = FontFamily(Font(R.font.pacifico))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Atlas",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = pacificoFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 32.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer // ✅ Gunakan warna teks sesuai tema
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer, // ✅ Warna Background TopAppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer // ✅ Warna teks mengikuti tema
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // ✅ Gunakan warna background dari tema
                .padding(paddingValues) // ✅ Hindari tertutup oleh TopAppBar
                .padding(16.dp)
        ) {
            // ✅ Box untuk menampilkan peta agar tidak menutupi teks di atasnya
            Box(modifier = Modifier.weight(1f)) {
                JournalMap(journals = journals)
            }
        }
    }
}

@Composable
fun JournalMap(journals: List<Journal>) {
    val context = LocalContext.current

    AndroidView(factory = { ctx ->
        val mapView = MapView(ctx)
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // ✅ Set posisi default ke Singapura
        val mapController = mapView.controller
        mapController.setZoom(10.0)
        mapController.setCenter(GeoPoint(1.3521, 103.8198))

        // ✅ Tambahkan marker dari jurnal
        journals.forEach { journal ->
            val parts = journal.location?.split(",")
            if (parts?.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lon = parts[1].trim().toDoubleOrNull()
                if (lat != null && lon != null) {
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(lat, lon)
                    marker.title = journal.title
                    marker.subDescription = journal.content
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    // ✅ Gunakan ikon default dari OSM
                    marker.icon = ctx.getDrawable(org.osmdroid.library.R.drawable.osm_ic_center_map)

                    mapView.overlays.add(marker)
                }
            }
        }
        mapView
    }, modifier = Modifier.fillMaxSize())
}
