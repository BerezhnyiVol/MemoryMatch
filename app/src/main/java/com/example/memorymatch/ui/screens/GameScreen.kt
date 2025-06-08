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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorymatch.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

data class CardData(
    val imageResId: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
fun GameScreen(players: Int, gridSize: Int, onBackToMenu: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val orientation = configuration.orientation

    val spacing = 8.dp
    val sizeReductionFactor = 0.85f

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

    val baseImages = listOf(
        R.drawable.cat, R.drawable.fox, R.drawable.frog, R.drawable.koala, R.drawable.monkey,
        R.drawable.mouse, R.drawable.turtle, R.drawable.bee, R.drawable.shark,
        R.drawable.jellyfish, R.drawable.parrot, R.drawable.snake, R.drawable.penguin, R.drawable.owl,
        R.drawable.horse, R.drawable.crab, R.drawable.pig, R.drawable.flamingo, R.drawable.hippopotamus,
        R.drawable.chameleon, R.drawable.ant, R.drawable.tiger, R.drawable.panda, R.drawable.whale
    )

    val totalCards = columns * rows
    val neededPairs = totalCards / 2
    val images = generateSequence { baseImages }.flatten().take(neededPairs).toList()
    val allCards = (images + images).shuffled()

    var cardsState by remember { mutableStateOf(allCards.map { CardData(it) }) }
    var revealed by remember { mutableStateOf<List<Int>>(emptyList()) }
    var currentPlayer by remember { mutableStateOf(1) }
    var p1Score by remember { mutableStateOf(0) }
    var p2Score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0) }
    var attempts by remember { mutableStateOf(0) }

    LaunchedEffect(players, gameOver) {
        if (players == 1 && !gameOver) {
            while (isActive) {
                delay(1000)
                elapsedTime++
            }
        }
    }

    LaunchedEffect(revealed) {
        if (revealed.size == 2) {
            attempts++
            val first = revealed[0]
            val second = revealed[1]
            delay(600)
            if (cardsState[first].imageResId == cardsState[second].imageResId) {
                cardsState = cardsState.mapIndexed { i, card ->
                    if (i == first || i == second)
                        card.copy(isMatched = true, isRevealed = true)
                    else card
                }
                if (players == 2) {
                    if (currentPlayer == 1) p1Score++ else p2Score++
                }
            } else {
                cardsState = cardsState.mapIndexed { i, card ->
                    if (i == first || i == second) card.copy(isRevealed = false)
                    else card
                }
                if (players == 2) {
                    currentPlayer = if (currentPlayer == 1) 2 else 1
                }
            }
            revealed = emptyList()
            if (cardsState.all { it.isMatched }) gameOver = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToMenu) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Späť")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Čas: ${elapsedTime}s",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (players == 2) {
                Text(
                    text = "Hráč 1 = $p1Score bodov | Hráč 2 = $p2Score bodov",
                    fontSize = 16.sp
                )
            }
        }

        // Плашка результата после окончания игры
        if (gameOver) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (players == 1) {
                    Text("🎉 Gratulujeme!", fontSize = 22.sp)
                    Text("Čas: ${elapsedTime} sekúnd", fontSize = 18.sp)
                    Text("Počet pokusov: $attempts", fontSize = 18.sp)
                } else {
                    val winnerText = when {
                        p1Score > p2Score -> "🎉 Vyhral hráč 1!"
                        p2Score > p1Score -> "🎉 Vyhral hráč 2!"
                        else -> "🤝 Remíza!"
                    }
                    Text(winnerText, fontSize = 22.sp)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize().padding(top = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing)
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
                .background(Color.LightGray)
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
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }
}
