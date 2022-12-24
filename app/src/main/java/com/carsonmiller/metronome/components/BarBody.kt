package com.carsonmiller.metronome.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun BarBody(modifier: Modifier = Modifier, currentNote: Int, numOfNotes: Int, playing: Boolean, bpm: Int, height: Dp) =
    BoxContainer(modifier = modifier,height, false) {
        val time = 60000 / bpm
        val progress by animateFloatAsState(
            targetValue =
            when {
                numOfNotes > 1 && playing -> currentNote / (numOfNotes.toFloat() - 1)
                playing -> 1f
                else -> 0f
            },
            animationSpec = tween(durationMillis = time)
        )
        LinearProgressIndicator(
            modifier = Modifier.height(height),
            progress = progress,
            trackColor = MaterialTheme.colorScheme.inversePrimary,
            color = MaterialTheme.colorScheme.primary
        )
    }