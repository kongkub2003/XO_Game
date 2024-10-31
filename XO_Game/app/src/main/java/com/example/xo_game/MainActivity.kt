    package com.example.xo_game

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.animation.core.LinearEasing
    import androidx.compose.animation.core.RepeatMode
    import androidx.compose.animation.core.animateFloat
    import androidx.compose.animation.core.infiniteRepeatable
    import androidx.compose.animation.core.rememberInfiniteTransition
    import androidx.compose.animation.core.tween
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Shapes
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import com.example.xo_game.ui.theme.Xo_GameTheme
    import io.realm.Realm
    import io.realm.RealmConfiguration


    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {


            super.onCreate(savedInstanceState)
            Realm.init(this)

            enableEdgeToEdge()
            setContent {
                Xo_GameTheme {
                    Navigation()
                }
            }
        }
        }

    @Composable
    fun MenuScreen(
        NagativetoConfig: () -> Unit,
        onNavigateToItemList:() -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition()

        val animatedFontSize by infiniteTransition.animateFloat(
            initialValue = 30f,
            targetValue = 40f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {

            Text(
                text = "TIC TAC TOE",
                fontSize = animatedFontSize.sp,
                fontWeight = FontWeight.Bold

            )
            Column(
                modifier = Modifier
                    .padding(100.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = NagativetoConfig,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red, // Background color
                        contentColor = Color.White    // Text color
                    ),
                    modifier = Modifier.size(width = 150.dp, height = 150.dp) // Set size here

                ) {

                    Text(text = "Play With Player")
                }
                Spacer(Modifier.padding(30.dp))

            }
            Button(onClick = onNavigateToItemList,
                    modifier = Modifier.size(170.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue, // Background color
            contentColor = Color.White    // Text color
            )) {
                Text(text = "History PLAY", modifier = Modifier.background(Color.Transparent))
            }
        }
        }

