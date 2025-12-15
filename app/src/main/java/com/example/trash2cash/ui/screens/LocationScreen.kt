package com.example.trash2cash.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.PorterDuff
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// Model Data Lokasi Bank Sampah
data class SampahLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(navController: NavController) {
    val context = LocalContext.current

    // Data lokasi statis
    val locations = remember {
        listOf(
            SampahLocation("Bank Sampah Dinoyo", -7.946111, 112.604028),
            SampahLocation("Bank Sampah Suhat", -7.948937, 112.622778),
            SampahLocation("Bank Sampah Pisang Kipas", -7.939447, 112.612589)
        )
    }

    // State untuk menyimpan lokasi pengguna
    var userLocation by remember { mutableStateOf<GeoPoint?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Launcher untuk meminta izin lokasi
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Izin diberikan, coba dapatkan lokasi
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let {
                            userLocation = GeoPoint(it.latitude, it.longitude)
                        }
                    }
            }
        }
    )

    // Minta izin saat Composable pertama kali ditampilkan
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Konfigurasi awal OSMDroid
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    Configuration.getInstance().userAgentValue = context.packageName

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lokasi Bank Sampah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val mapView = rememberMapViewWithLifecycle()

        AndroidView(
            factory = { mapView },
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            update = { map ->
                map.setTileSource(TileSourceFactory.MAPNIK)
                map.setMultiTouchControls(true)

                map.controller.setZoom(15.0)
                if (locations.isNotEmpty()) {
                    val startPoint = GeoPoint(locations.first().latitude, locations.first().longitude)
                    map.controller.setCenter(startPoint)
                }

                // Hapus overlay lama sebelum menambahkan yang baru
                map.overlays.clear()

                // Tambahkan marker untuk setiap bank sampah
                locations.forEach { location ->
                    val marker = Marker(map).apply {
                        position = GeoPoint(location.latitude, location.longitude)
                        title = location.name
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        // Menggunakan ikon default (biru)
                    }
                    map.overlays.add(marker)
                }

                // Tambahkan marker untuk lokasi pengguna jika tersedia
                userLocation?.let {
                    val userMarker = Marker(map).apply {
                        position = it
                        title = "Lokasi Anda"
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        
                        // Mengubah warna ikon default menjadi merah
                        val userMarkerIcon = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.marker_default)?.mutate()
                        userMarkerIcon?.setColorFilter(Color.Red.toArgb(), PorterDuff.Mode.SRC_IN)
                        icon = userMarkerIcon
                    }
                    map.overlays.add(userMarker)
                }

                map.invalidate() // Refresh peta untuk menampilkan marker
            }
        )
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}
