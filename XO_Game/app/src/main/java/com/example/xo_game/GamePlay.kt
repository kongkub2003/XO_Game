package com.example.xo_game



import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xo_game.history.DateRecord
import com.example.xo_game.history.GameRecord
import com.example.xo_game.history.GameSession
import com.example.xo_game.history.MoveLog
import com.example.xo_game.history.fileName
import com.example.xo_game.history.readJsonFromFile
import com.example.xo_game.history.saveJsonToFile
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PlayTicTacToe(
    context: Context,
    onBackToXOConfigPlays: () -> Unit,
    navController: NavController,
    field: Int,
    line: Int
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cellSize = screenWidth / field
    val dateRecords: DateRecord

    var board by remember { mutableStateOf(List(field) { MutableList(field) { "" } }) }
    var currentPlayerSymbol by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var fullbord by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var moveNumber by remember { mutableIntStateOf(0) }
    val moveHistory = remember { mutableStateListOf<MoveLog>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (winner != null) {
            Text(text = "Winner: Player ${if (winner == "X") "1" else "2"}", fontSize = 28.sp)
        } else if (fullbord) {
            Text(text = "It's a draw!", fontSize = 28.sp)
        } else {
            Text(
                text = "Current Turn: Player ${if (currentPlayerSymbol == "X") "1" else "2"}",
                fontSize = 28.sp
            )
        }

        for (y in 0 until field) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (x in 0 until field) {
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .padding(4.dp)
                            .background(Color.Gray, RoundedCornerShape(4.dp))
                            .clickable(enabled = board[y][x].isEmpty() && winner == null) {
                                board = board.toMutableList().apply { this[y][x] = currentPlayerSymbol }
                                moveHistory.add(MoveLog(player = currentPlayerSymbol, row = y, column = x))

                                // Check for winner or draw
                                winner = checkwin(field, line, board)
                                fullbord = isBoardFull(board)
                                if (winner == null && !fullbord) {
                                    currentPlayerSymbol = if (currentPlayerSymbol == "X") "O" else "X"

                                    moveNumber++

                                } else {
                                    showDialog = true
                                    val gson = Gson()
                                    val dateRecords: DateRecord
                                    val gameSession = GameSession(boardSize = field, boardLine = line , moves = moveHistory)
                                    val currentDateTime = LocalDateTime.now()
                                    val formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss")

                                    val oldJson = readJsonFromFile(context, fileName)
                                    val formattedTime = currentDateTime.format(formatter)

                                    val gameRecord = GameRecord(time = formattedTime, gameSessions = listOf(gameSession))

                                    if (oldJson != null) {
                                        dateRecords = gson.fromJson(oldJson, DateRecord::class.java)
                                        dateRecords.date.add(gameRecord)
                                    } else {
                                        dateRecords = DateRecord(date = mutableListOf(gameRecord))
                                    }
                                    val jsonLog = gson.toJson(dateRecords)
                                    saveJsonToFile(context, jsonLog, fileName)
                                    //Move
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[y][x],
                            fontSize = 36.sp,
                            color = when (board[y][x]) {
                                "X" -> Color(0xFFC7F089)
                                "O" -> Color(0xFFFF073A)
                                else -> Color.Black
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = if (winner != null) "Winner: Player ${if (winner == "X") "1" else "2"}" else "It's a draw!") },
                text = { Text("Do you want to play again?") },
                confirmButton = {
                    Button(onClick = {
                        board = List(field) { MutableList(field) { "" } }
                        currentPlayerSymbol = "X"
                        winner = null
                        fullbord = false
                        showDialog = false
                        //moveHistory.clear()
                        moveNumber = 0
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                        navController.navigate("Menu") // Navigate to Menu screen
                    }) {
                        Text("No")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBackToXOConfigPlays,
            modifier = Modifier.padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Exit")
        }
    }
}






fun isBoardFull(board: List<List<String>>): Boolean {
    return board.all { row -> row.all { it.isNotEmpty() } } // Check if all cells are filled
}
fun checkwin(field: Int, line: Int, board: List<List<String>>): String? {
    // Check rows for winner
    for (y in 0 until field) {
        if (board[y].all { it == "X" }) return "X"
        if (board[y].all { it == "O" }) return "O"
    }

    // Check columns for winner
    for (x in 0 until field) {
        if (board.all { it[x] == "X" }) return "X"
        if (board.all { it[x] == "O" }) return "O"
    }

    // Check diagonals for winner
    if ((0 until field).all { board[it][it] == "X" }) return "X" // Top-left to bottom-right
    if ((0 until field).all { board[it][field - 1 - it] == "X" }) return "X" // Top-right to bottom-left
    if ((0 until field).all { board[it][it] == "O" }) return "O" // Top-left to bottom-right
    if ((0 until field).all { board[it][field - 1 - it] == "O" }) return "O" // Top-right to bottom-left

    return null // No winner yet

}
