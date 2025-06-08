package com.example.memorymatch.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorymatch.R
import com.example.memorymatch.data.SoundPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

// Popis: Dátová trieda reprezentujúca jednu kartu v hre
data class CardData(
    val imageResId: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)

// Popis: Hlavná obrazovka hry s logikou pre dvojhru, skóre a herný režim
@Composable
fun GameScreen(players: Int, gridSize: Int, mode: String, onBackToMenu: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val orientation = configuration.orientation

    val spacing = 8.dp
    val sizeReductionFactor = 0.85f

    // Popis: Výpočet rozmerov mriežky podľa orientácie zariadenia
    val columns: Int
    val rows: Int

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        columns = 4
        rows = gridSize
    } else {
        columns = gridSize
        rows = 4
    }

    val availableWidth = screenWidth - (spacing * (columns + 1))
    val availableHeight = screenHeight - (spacing * (rows + 1))

    val cardSize: Dp = with(LocalDensity.current) {
        minOf(availableWidth / columns, availableHeight / rows) * sizeReductionFactor
    }

    // Popis: Príprava obrázkov a zamiešanie kariet
    val baseImages = listOf(
        R.drawable.cat, R.drawable.fox, R.drawable.frog, R.drawable.koala, R.drawable.monkey,
        R.drawable.mouse, R.drawable.jellyfish, R.drawable.parrot,
        R.drawable.penguin, R.drawable.owl,
        R.drawable.horse, R.drawable.crab, R.drawable.flamingo, R.drawable.hippopotamus,
        R.drawable.chameleon, R.drawable.panda
    )

    val totalCards = columns * rows
    val neededPairs = totalCards / 2
    val images = generateSequence { baseImages }.flatten().take(neededPairs).toList()
    val allCards = (images + images).shuffled()

    // Popis: Stavové premenne hry
    var cardsState by remember { mutableStateOf(allCards.map { CardData(it) }) }
    var revealed by remember { mutableStateOf<List<Int>>(emptyList()) }
    var currentPlayer by remember { mutableStateOf(1) }
    var p1Score by remember { mutableStateOf(0) }
    var p2Score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }
    var attempts by remember { mutableStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Popis: Easy mód - krátke odhalenie všetkých kariet na začiatku
    LaunchedEffect(Unit) {
        if (mode == "easy") {
            cardsState = cardsState.map { it.copy(isRevealed = true) }
            delay(3000)
            cardsState = cardsState.map { it.copy(isRevealed = false) }
        }
        gameStarted = true
    }

    // Popis: Počítanie času počas hry
    LaunchedEffect(gameStarted, players, gameOver) {
        if (gameStarted && !gameOver) {
            while (isActive) {
                delay(1000)
                elapsedTime++
            }
        }
    }

    // Popis: Herná logika pri odhalení páru kariet
    LaunchedEffect(revealed) {
        if (revealed.size == 2) {
            attempts++
            val first = revealed[0]
            val second = revealed[1]
            delay(600)
            if (cardsState[first].imageResId == cardsState[second].imageResId) {
                SoundPlayer.playCorrectPairSound(context)
                cardsState = cardsState.mapIndexed { i, card ->
                    if (i == first || i == second) card.copy(isMatched = true, isRevealed = true) else card
                }
                if (players == 2) {
                    if (currentPlayer == 1) p1Score++ else p2Score++
                }
            } else {
                SoundPlayer.playWrongPairSound(context)
                cardsState = cardsState.mapIndexed { i, card ->
                    if (i == first || i == second) card.copy(isRevealed = false) else card
                }
                if (players == 2) {
                    currentPlayer = if (currentPlayer == 1) 2 else 1
                }
            }
            revealed = emptyList()
            if (cardsState.all { it.isMatched }) {
                gameOver = true
                SoundPlayer.playGameOverSound(context)
            }
        }
    }

    // Popis: Celá hlavná obrazovka
    Box(modifier = Modifier.fillMaxSize()) {

        val contentScale = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ContentScale.Crop else ContentScale.FillHeight

        Image(
            painter = painterResource(id = R.drawable.game_background),
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D1B2A).copy(alpha = 0.15f))
        )

        // Popis: Horný panel so skóre a časom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToMenu) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.cas) + " ${elapsedTime}s", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "|")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.attempts) + " $attempts", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))

            if (players == 2) {
                Text(
                    text = stringResource(R.string.hrac_1) + "$p1Score" +
                            stringResource(R.string.bodov_hr_2) + "$p2Score" +
                            stringResource(R.string.bodov),
                    fontSize = 16.sp
                )
            }
        }

        // Popis: Zobrazenie dialógu po skončení hry
        if (gameOver) {
            AlertDialog(
                onDismissRequest = { onBackToMenu() },
                title = { Text(text = stringResource(R.string.game_over), fontSize = 26.sp) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (players == 2) {
                            val winnerText = when {
                                p1Score > p2Score -> stringResource(R.string.player_1_wins)
                                p2Score > p1Score -> stringResource(R.string.player_2_wins)
                                else -> stringResource(R.string.draw)
                            }
                            Text(winnerText, fontSize = 22.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text("${stringResource(R.string.cas)}: ${elapsedTime} s", fontSize = 20.sp)
                        Text("${stringResource(R.string.attempts)}: $attempts", fontSize = 20.sp)
                    }
                },
                confirmButton = {
                    Button(onClick = { onBackToMenu() }) {
                        SoundPlayer.playButtonSound(context)
                        Text(stringResource(R.string.back_to_menu))
                    }
                }
            )
        }

        // Popis: Hlavná mriežka kariet
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.Center,
        ) {
            items(cardsState.size) { index ->
                val card = cardsState[index]
                MemoryCard(
                    imageResId = card.imageResId,
                    isRevealed = card.isRevealed,
                    isMatched = card.isMatched,
                    cardSize = cardSize,
                    onClick = {
                        if (!card.isRevealed && !card.isMatched && revealed.size < 2) {
                            SoundPlayer.playCardFlipSound(context)
                            cardsState = cardsState.toMutableList().apply {
                                this[index] = this[index].copy(isRevealed = true)
                            }
                            revealed = revealed + index
                        }
                    }
                )
            }
        }
    }
}

// Popis: Komponent reprezentujúci jednu kartu v mriežke
@Composable
fun MemoryCard(
    imageResId: Int,
    isRevealed: Boolean,
    isMatched: Boolean,
    cardSize: Dp,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isRevealed || isMatched) 180f else 0f, label = "")
    val showFront = rotation > 90f
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .size(cardSize)
            .graphicsLayer { rotationY = rotation; cameraDistance = 8 * density.density }
            .clickable(enabled = !isRevealed && !isMatched) { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFF0D1B2A).copy(alpha = 0.03f))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val image = when {
                isMatched -> R.drawable.correct
                showFront -> imageResId
                else -> R.drawable.card
            }
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}
