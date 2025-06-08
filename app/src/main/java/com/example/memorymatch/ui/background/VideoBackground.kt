package com.example.memorymatch.ui.background

import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout

// Popis: Kompozitný komponent na prehrávanie videa na pozadí pomocou ExoPlayer
@OptIn(UnstableApi::class)
@Composable
fun VideoBackground(modifier: Modifier = Modifier, context: Context) {

    // Popis: Inicializácia ExoPlayer a nastavenie videa na opakované prehrávanie
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/background")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    // Popis: Uvoľnenie ExoPlayer pri zániku kompozície
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Popis: Vloženie PlayerView do Compose stromu pomocou AndroidView
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                alpha = 0.3f
                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM)
            }
        }
    )
}
