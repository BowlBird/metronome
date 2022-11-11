package com.carsonmiller.metronome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carsonmiller.metronome.PersistentMusicSettings
import com.carsonmiller.metronome.ScreenSettings

@Composable
fun ButtonBody(modifier: Modifier = Modifier, settings: PersistentMusicSettings) = Row(
    modifier = modifier.padding(5.dp), horizontalArrangement = Arrangement.Center
) {
    val buttonSize = 50.dp
    fun buttonModifier(size: Dp) = Modifier
        .align(Alignment.CenterVertically)
        .padding(ScreenSettings().innerPadding / 2) //since these objects are right next to each other it would be 20 otherwise
        .size(size)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm -= 4
    }, contents = {}, isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm -= 1
    }, contents = {},isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize * 1.2f), onClick = {
        settings.numerator -= 0
    }, contents = {},isHoldable = false)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm += 1
    }, contents = {},isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm += 4
    }, contents = {},isHoldable = true)
}
