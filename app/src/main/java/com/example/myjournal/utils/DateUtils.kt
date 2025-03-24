package com.example.myjournal.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
