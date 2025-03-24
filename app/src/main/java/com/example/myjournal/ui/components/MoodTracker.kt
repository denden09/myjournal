package com.example.myjournal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myjournal.model.MoodEntry

@Composable
fun MoodTrackerChart(
    moodData: List<MoodEntry>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Mood Tracker (Past 7 Days)",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {

            val barWidth = size.width / (moodData.size * 2)
            val maxMoodLevel = 3f // Maksimal mood level kamu

            moodData.forEachIndexed { index, moodEntry ->

                val moodValue = moodEntry.moodLevel.toFloat()
                val barHeight = (moodValue / maxMoodLevel) * size.height

                // ✅ Tentukan warna berdasarkan moodLevel
                val barColor = when (moodEntry.moodLevel) {
                    1 -> Color.Red       // Mood buruk
                    2 -> Color.Yellow    // Mood sedang
                    3 -> Color.Green     // Mood baik
                    else -> Color.Gray
                }

                drawRoundRect(
                    color = barColor,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x = index * barWidth * 2,
                        y = size.height - barHeight
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        width = barWidth,
                        height = barHeight
                    ),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            moodData.forEach {
                Text(text = it.day)
            }
        }
    }
}
