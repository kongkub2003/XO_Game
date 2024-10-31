package com.example.xo_game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun SettingtoPlay(
    onNavigateToXOPlayScreen: (Int, Int) -> Unit,
    Menu: () -> Unit
) {
    var fieldText by remember { mutableStateOf("") }
    var lineText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Setting Table", fontSize = 30.sp, modifier = Modifier.height(100.dp))

        TextField(
            value = fieldText,
            onValueChange = { fieldText = it },
            label = { Text(" FILED SIZE IN PLAY ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                val fieldSize = fieldText.toIntOrNull() ?: 3
                val lineLength = lineText.toIntOrNull() ?: 3
                onNavigateToXOPlayScreen(fieldSize, lineLength) // Pass values to navigate
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(50.dp)
                .width(150.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF476810),
                            Color(0xFFFFFFFF)
                        )
                    )
                ),
            
            colors = ButtonDefaults.buttonColors(Color.Transparent),
        ) {
            Text("Play", color = Color.White)
        }

        Button(
            onClick = Menu,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(50.dp)
                .width(150.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF324F00), Color(0xFF213600))
                    )
                ),
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text("Back", color = Color.White)
        }
    }
}