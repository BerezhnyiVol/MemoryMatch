package com.example.memorymatch.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorymatch.R
import com.example.memorymatch.ui.theme.MemoryMatchTheme
import kotlinx.coroutines.delay

data class CardData(
    val imageResId: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
fun GameScreen(players: Int, gridSize: Int, onBackToMenu: () -> Unit) {
    val spacing = 8.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardSize = with(LocalDensity.current) {
        (screenWidth - (spacing * (gridSize + 1))) / gridSize
    }

    val baseImages = listOf(
        R.drawable.cat, R.drawable.fox, R.drawable.frog,
        R.drawable.koala, R.drawable.monkey, R.drawable.mouse,
        R.drawable.turtle, R.drawable.turtle
    )

    val totalCards = gridSize * gridSize
    val neededPairs = totalCards / 2
    val images = generateSequence { baseImages }.flatten().take(neededPairs).toList()
    val allCards = (images + images).shuffled()

    var cardsState by remember { mutableStateOf(allCards.map { CardData(it) }) }
    var revealed by remember { mutableStateOf<List<Int>>(emptyList()) }
    var currentPlayer by remember { mutableStateOf(1) }
    var p1Score by remember { mutableStateOf(0) }
    var p2Score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }

    LaunchedEffect(revealed) {
        if (revealed.size == 2) {
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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Memory Match", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))

        if (gameOver) {
            Text(
                text = when {
                    players == 1 -> "🎉 Gratulujeme!"
                    p1Score > p2Score -> "🎉 Vyhral hráč 1!"
                    p2Score > p1Score -> "🎉 Vyhral hráč 2!"
                    else -> "🤝 Remíza!"
                },
                fontSize = 24.sp
            )
            if (players == 2) {
                Text("Hráč 1: $p1Score | Hráč 2: $p2Score")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBackToMenu) {
                Text("Späť do menu")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                modifier = Modifier.fillMaxWidth(),
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

            if (players == 2) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Hráč 1: $p1Score bodov | Hráč 2: $p2Score bodov")
                Text("Na ťahu: Hráč $currentPlayer")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBackToMenu) {
            Text("Späť do menu")
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
    val rotation by animateFloatAsState(
        targetValue = if (isRevealed || isMatched) 180f else 0f,
        label = "flip"
    )
    val showFront = rotation > 90f
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .size(cardSize)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density.density
            }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    MemoryMatchTheme {
        GameScreen(players = 1, gridSize = 4, onBackToMenu = {})
    }
}
