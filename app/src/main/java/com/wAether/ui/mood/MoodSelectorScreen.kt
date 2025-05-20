package com.wAether.ui.mood

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

// Define the list of moods available for selection
val moodList = listOf(
    "😊 Calm",
    "😟 Anxious",
    "⚡ Energized",
    "😌 Relaxed",
    "😄 Happy",
    "😢 Sad",
    "😠 Stressed",
    "🥱 Tired"
)

@Composable
fun MoodSelectorScreen(
    onMoodSelected: (String) -> Unit,
    onCancel: () -> Unit
) {
    // ScalingLazyColumn is often preferred for Wear OS lists as it provides a fisheye effect
    // making items at the center larger and easier to tap.
    // However, for simplicity and to show a basic list, LazyColumn is used here.
    // You can replace LazyColumn with ScalingLazyColumn for a more Wear-native feel.
    // e.g., ScalingLazyColumn(modifier = Modifier.fillMaxSize()) { ... }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp), // Add some padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you feeling?",
                style = MaterialTheme.typography.title3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f), // Takes up available space
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
            ) {
                items(moodList) { mood ->
                    Button(
                        onClick = { onMoodSelected(mood.substringAfter(" ")) }, // Pass only the mood name
                        modifier = Modifier.fillMaxWidth(0.8f), // Make buttons a bit wider
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.surface // Use theme surface color
                        )
                    ) {
                        Text(text = mood, fontSize = 14.sp)
                    }
                }
            }

            // Cancel Button
            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(ButtonDefaults.SmallButtonSize)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cancel mood selection",
                    tint = Color.Gray // Softer color for cancel
                )
            }
        }
    }
}

// --- Previews for Android Studio ---
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun MoodSelectorScreenPreview() {
    // Assuming WAetherTheme is available from MainActivity.kt or a Theme.kt file
    com.wAether.WAetherTheme {
        MoodSelectorScreen(onMoodSelected = { mood -> println("Preview: $mood selected") }, onCancel = {})
    }
}
