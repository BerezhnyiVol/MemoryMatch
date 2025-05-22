package com.example.memorymatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorymatch.ui.background.VideoBackground


@Composable
fun MainMenuScreen(onStartGame: (Int, Int) -> Unit) {
    var selectedPlayers by remember { mutableStateOf(1) }
    var selectedGridSize by remember { mutableStateOf(4) }
    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackground(modifier = Modifier.fillMaxSize(), context = context)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Memory Match",
                fontSize = 50.sp,
                color = Color.White,
                modifier = Modifier.shadow(2.dp)
            )


            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Počet hráčov:",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.shadow(2.dp)
            )


            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = { selectedPlayers = 1 },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = if (selectedPlayers == 1)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                    else
                        ButtonDefaults.buttonColors()
                ) {

                    Text("1 hráč")
                }
                Button(
                    onClick = { selectedPlayers = 2 },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = if (selectedPlayers == 2)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("2 hráči")
                }
            }



            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Veľkosť mriežky:",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.shadow(2.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { selectedGridSize = 4 },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = if (selectedGridSize == 4)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("4×4")
                }

                Button(
                    onClick = { selectedGridSize = 6 },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = if (selectedGridSize == 6)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("6×6")
                }

                Button(
                    onClick = { selectedGridSize = 8 },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp),
                    colors = if (selectedGridSize == 8)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("8×8")
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onStartGame(selectedPlayers, selectedGridSize) },

                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text("Spustiť hru")
            }
            Spacer(modifier = Modifier.height(32.dp))

            //statistica
            Button(
                onClick = { onStartGame(selectedPlayers, selectedGridSize) },

                modifier = Modifier.fillMaxWidth().height(50.dp),
            ) {
                Text("Statistika")
            }
        }
    }
}
