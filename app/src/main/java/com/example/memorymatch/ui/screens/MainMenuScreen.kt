package com.example.memorymatch.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorymatch.ui.background.VideoBackground
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

@Composable
fun MainMenuScreen(onStartGame: (Int, Int) -> Unit, onStatistics: () -> Unit) {
    var selectedPlayers by rememberSaveable { mutableStateOf(1) }
    var selectedGridSize by rememberSaveable { mutableStateOf(4) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(modifier = Modifier.fillMaxSize()) {
        VideoBackground(
            modifier = Modifier.fillMaxSize(),
            context = context
        )

        if (isPortrait) {
            // Портретная ориентация
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Spacer(modifier = Modifier.height(32.dp))
                PlayerSelection(selectedPlayers) { selectedPlayers = it }
                Spacer(modifier = Modifier.height(32.dp))
                GridSelection(selectedGridSize) { selectedGridSize = it }
                Spacer(modifier = Modifier.height(32.dp))
                StartButtons(selectedPlayers, selectedGridSize, onStartGame, onStatistics)
            }
        } else {
            // Ландшафтная ориентация — окончательная чистая версия
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerSelection(selectedPlayers) { selectedPlayers = it }
                    GridSelection(selectedGridSize) { selectedGridSize = it }
                }

                StartButtons(selectedPlayers, selectedGridSize, onStartGame, onStatistics)
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Memory Match",
        fontSize = 50.sp,
        color = Color.White,
        modifier = Modifier.shadow(2.dp)
    )
}

@Composable
fun PlayerSelection(selected: Int, onSelect: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Počet hráčov:",
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.shadow(2.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            PlayerButton(1, selected) { onSelect(1) }
            PlayerButton(2, selected) { onSelect(2) }
        }
    }
}

@Composable
fun GridSelection(selected: Int, onSelect: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Veľkosť mriežky:",
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.shadow(2.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            GridButton(4, selected) { onSelect(4) }
            GridButton(6, selected) { onSelect(6) }
            GridButton(8, selected) { onSelect(8) }
        }
    }
}

@Composable
fun StartButtons(selectedPlayers: Int, selectedGridSize: Int, onStartGame: (Int, Int) -> Unit, onStatistics: () -> Unit) {
    Button(
        onClick = { onStartGame(selectedPlayers, selectedGridSize) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text("Spustiť hru")
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onStatistics,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text("Statistika")
    }
}

@Composable
fun PlayerButton(player: Int, selected: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = if (player == selected)
            ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        else
            ButtonDefaults.buttonColors()
    ) {
        Text("$player hráč" + if (player == 2) "i" else "")
    }
}

@Composable
fun GridButton(size: Int, selected: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = if (size == selected)
            ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        else
            ButtonDefaults.buttonColors()
    ) {
        Text("${size}×${size}")
    }
}
