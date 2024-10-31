package com.example.xo_game.history

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileInputStream
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun List_History(
    context: Context,
    onItemSelected: (GameRecord) -> Unit,
    Menu: () -> Unit,
) {
    val items = remember { mutableStateListOf<GameRecord>() }

    LaunchedEffect(Unit) {
        val file = File(context.filesDir, fileName)
        if (file.exists()) {
            try {
                val inputStream = FileInputStream(file)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val jsonText = bufferedReader.use { it.readText() }
                val itemType = object : TypeToken<DateRecord>() {}.type
                val dateRecord: DateRecord = Gson().fromJson(jsonText, itemType)
                items.clear()

                items.addAll(dateRecord.date.sortedByDescending { it.time })
            } catch (e: Exception) {
                items.clear()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(0.05f))

        if (items.isEmpty()) { Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp),
            contentAlignment = Alignment.Center
        ){
            Text("No game history available", fontSize = 18.sp, fontWeight = FontWeight.Medium,)
        } }else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items) { gameRecord ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemSelected(gameRecord) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Use the helper function to format the date
                            val formattedTime = formatDateTime(gameRecord.time)

                          Text(
                                text = "Last Played: $formattedTime",
                             //modifier = Modifier.weight(1f)
                          )
                        }
                    }
                }
            }
        }

        Button(onClick = { Menu() }, colors = ButtonDefaults.buttonColors(
            containerColor = Color.DarkGray, // Background color
            contentColor = Color.White    // Text color
        )) {
            Text("Back to Menu")
        }
    }
}


fun formatDateTime(dateString: String): String {
    return try {
        // Assuming dateString is in "yyyy-MM-dd HH:mm:ss" format
        val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val desiredFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val parsedDate = LocalDateTime.parse(dateString, originalFormatter)
        parsedDate.format(desiredFormatter)
    } catch (e: Exception) {
        "" // Return an empty string if the format is invalid
    }
}