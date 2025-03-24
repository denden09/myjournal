package com.example.myjournal.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToCacheAndGetUri(bitmap: Bitmap, context: Context): Uri? {
    return try {
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
