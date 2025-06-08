package com.example.memorymatch.ui.screens

import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.res.stringResource
import com.example.memorymatch.R
import com.example.memorymatch.data.LanguagePreferences
import com.example.memorymatch.data.SoundPlayer

// Popis: Hlavná obrazovka hlavného menu s výberom nastavení hry
@Composable
fun MainMenuScreen(onStartGame: (Int, Int, String) -> Unit) {
    var selectedPlayers by rememberSaveable { mutableStateOf(1) }
    var selectedGridSize by rememberSaveable { mutableStateOf(4) }
    var selectedMode by rememberSaveable { mutableStateOf("classic") }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Box(modifier = Modifier.fillMaxSize()) {
        // Popis: Animované video na pozadí hlavného menu
        VideoBackground(modifier = Modifier.fillMaxSize(), context = context)

        if (isPortrait) {
            // Popis: Rozloženie pre vertikálnu orientáciu
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
                ModeSelection(selectedMode) { selectedMode = it }
                Spacer(modifier = Modifier.height(32.dp))
                LanguageSwitcher(context)
                Spacer(modifier = Modifier.height(32.dp))
                StartButton(selectedPlayers, selectedGridSize, selectedMode, onStartGame)
            }
        } else {
            // Popis: Rozloženie pre horizontálnu orientáciu
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModeSelection(selectedMode) { selectedMode = it }
                    LanguageSwitcher(context)
                }

                StartButton(selectedPlayers, selectedGridSize, selectedMode, onStartGame)
            }
        }
    }
}

// Popis: Názov aplikácie v menu
@Composable
fun Title() {
    Text(
        text = stringResource(R.string.memory_match),
        fontSize = 50.sp,
        color = Color.White,
        modifier = Modifier.shadow(2.dp)
    )
}

// Popis: Výber počtu hráčov
@Composable
fun PlayerSelection(selected: Int, onSelect: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.pocet_hracov),
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

// Popis: Výber veľkosti mriežky
@Composable
fun GridSelection(selected: Int, onSelect: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.velkost_mriezky),
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

// Popis: Výber herného módu
@Composable
fun ModeSelection(selected: String, onSelect: (String) -> Unit) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.game_mode),
            fontSize = 30.sp,
            color = Color.White,
            modifier = Modifier.shadow(2.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ModeButton(stringResource(R.string.classic), selected, context) { onSelect("classic") }
            ModeButton(stringResource(R.string.easy), selected, context) { onSelect("easy") }
        }
    }
}

// Popis: Tlačidlo pre výber módu
@Composable
fun ModeButton(mode: String, selected: String, context: Context, onClick: () -> Unit) {
    val buttonText = if (mode == "classic") stringResource(R.string.classic_button)
    else stringResource(R.string.easy_button)

    Button(
        onClick = { SoundPlayer.playButtonSound(context); onClick() },
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = if (mode == selected)
            ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        else ButtonDefaults.buttonColors()
    ) {
        Text(buttonText)
    }
}

// Popis: Prepinanie jazyka aplikácie
@Composable
fun LanguageSwitcher(context: Context) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.language), color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            LanguageButton(context, stringResource(R.string.sk), stringResource(R.string.sk_sk))
            LanguageButton(context, stringResource(R.string.en), stringResource(R.string.en_gb))
        }
    }
}

// Popis: Tlačidlo na zmenu jazyka
@Composable
fun LanguageButton(context: Context, lang: String, label: String) {
    Button(
        onClick = {
            SoundPlayer.playButtonSound(context)
            LanguagePreferences.saveLanguage(context, lang)
            (context as Activity).recreate()
        },
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
    ) { Text(label) }
}

// Popis: Štartovacie tlačidlo na spustenie hry
@Composable
fun StartButton(selectedPlayers: Int, selectedGridSize: Int, selectedMode: String, onStartGame: (Int, Int, String) -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = { SoundPlayer.playStartSound(context); onStartGame(selectedPlayers, selectedGridSize, selectedMode) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(stringResource(R.string.spustit_hru))
    }
}

// Popis: Tlačidlá pre výber počtu hráčov
@Composable
fun PlayerButton(player: Int, selected: Int, onClick: () -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = { SoundPlayer.playButtonSound(context); onClick() },
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = if (player == selected)
            ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        else ButtonDefaults.buttonColors()
    ) {
        Text("$player" + stringResource(R.string.hrac) + if (player == 2) "i" else "")
    }
}

// Popis: Tlačidlá pre výber veľkosti mriežky
@Composable
fun GridButton(size: Int, selected: Int, onClick: () -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = { SoundPlayer.playButtonSound(context); onClick() },
        modifier = Modifier
            .width(120.dp)
            .height(50.dp),
        colors = if (size == selected)
            ButtonDefaults.buttonColors(containerColor = Color(0xFF90CAF9))
        else ButtonDefaults.buttonColors()
    ) {
        Text("4×$size")
    }
}
