package com.example.myjournal.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

object LocationUtils {

    fun getLastKnownLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // ✅ Cek apakah permission lokasi sudah diberikan
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // ✅ Kalau tidak ada permission sama sekali, return null
        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            return null
        }

        // ✅ Ambil lokasi, prefer GPS, fallback ke NETWORK
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}
