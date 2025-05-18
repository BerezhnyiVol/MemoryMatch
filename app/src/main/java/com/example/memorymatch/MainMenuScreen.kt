package com.example.memorymatch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.memorymatch.ui.theme.MemoryMatchTheme

@Composable
fun MainMenuScreen(onStartGame: (Int, Int) -> Unit) {
    var selectedPlayers by remember { mutableStateOf(1) }
    var selectedGridSize by remember { mutableStateOf(4) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "MemoryMatch", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Počet hráčov:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { selectedPlayers = 1 },
                colors = if (selectedPlayers == 1)
                    ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                else
                    ButtonDefaults.buttonColors()
            ) {
                Text("1 hráč")
            }
            Button(
                onClick = { selectedPlayers = 2 },
                colors = if (selectedPlayers == 2)
                    ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                else
                    ButtonDefaults.buttonColors()
            ) {
                Text("2 hráči")
            }
        }



        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Veľkosť mriežky:")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { selectedGridSize = 4 },
                colors = if (selectedGridSize == 4)
                    ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                else
                    ButtonDefaults.buttonColors()
            ) {
                Text("4×4")
            }

            Button(
                onClick = { selectedGridSize = 6 },
                colors = if (selectedGridSize == 6)
                    ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
                else
                    ButtonDefaults.buttonColors()
            ) {
                Text("6×6")
            }

            Button(
                onClick = { selectedGridSize = 8 },
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Spustiť hru")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    MemoryMatchTheme {
        MainMenuScreen(onStartGame = { _, _ -> })
    }
}
