package com.example.xo_game.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Detail_History(
    item: GameRecord?,
    onBackToItemList: () -> Unit,
    Menu: () -> Unit,
) {
    var moveIndex by remember { mutableStateOf(item?.gameSessions?.firstOrNull()?.moves?.lastIndex ?: 0) }
    var isPlaying by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            coroutineScope.launch {
                while (isPlaying && moveIndex < (item?.gameSessions?.firstOrNull()?.moves?.size ?: 0) - 1) {
                    delay(500.milliseconds) // Adjust delay for speed control
                    moveIndex++
                }
                isPlaying = false // Stop when reaching the end
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item?.let { record_game ->
            Text(text = "Time: ${record_game.time}")
            record_game.gameSessions.forEachIndexed { sessionIndex, session ->
                Text(text = "lineLength: ${session.boardLine ?: "-"}")
                ShowBordGame(session, moveIndex)
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {

                    Button(onClick = { moveIndex = -1 }) {
                        Text("Reset")
                    }

                    Button(onClick = { isPlaying = !isPlaying }) {
                        Text(if (isPlaying) "Pause" else "Play")
                    }
                }
            }
        }

        Button(onClick = Menu) {
            Text("Back to Menu")
        }
    }
}

@Composable
fun ShowBordGame(session: GameSession, moveIndex: Int) {
    val field = session.boardSize
    val board = Array(field) { Array(field) { "" } }

    session.moves.take(moveIndex + 1).forEach { move ->
        board[move.row][move.column] = move.player
    }

    Column {
        for (i in 0 until field) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (j in 0 until field) {
                    Box(
                        modifier = Modifier
                            .size(75.dp)
                            .padding(4.dp)
                            .background(Color.Black, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[i][j],
                            fontSize = 36.sp,
                            color = when (board[i][j]) {
                                "X" -> Color(0xFFC7F089)
                                "O" -> Color(0xFFFF7043)
                                else -> Color.Black
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}