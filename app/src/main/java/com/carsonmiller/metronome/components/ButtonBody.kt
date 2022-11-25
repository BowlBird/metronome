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
import com.carsonmiller.metronome.state.PersistentMusicSegment
import com.carsonmiller.metronome.state.ScreenSettings

@Composable
fun ButtonBody(modifier: Modifier = Modifier, settings: PersistentMusicSegment) = Row(
    modifier = modifier.padding(5.dp), horizontalArrangement = Arrangement.Center
) {
    val buttonSize = 50.dp
    fun buttonModifier(size: Dp) = Modifier
        .align(Alignment.CenterVertically)
        .padding(ScreenSettings.innerPadding / 2) //since these objects are right next to each other it would be 20 otherwise
        .size(size)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm = getRoundedMetronomeBPM(settings.bpm, false)
    }, contents = {}, isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm -= 1
    }, contents = {}, isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize * 1.2f), onClick = {
        settings.numerator -= 0
    }, contents = {}, isHoldable = false)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm += 1
    }, contents = {}, isHoldable = true)
    MusicButton(modifier = buttonModifier(buttonSize), onClick = {
        settings.bpm = getRoundedMetronomeBPM(settings.bpm, true)
    }, contents = {}, isHoldable = true)
}

/**
 * Returns the next value that appears on physical metronomes
 * If False, returns the previous value that appears on a physical metronome
 */
fun getRoundedMetronomeBPM(bpm: Int, nextValue: Boolean): Int {
    if (bpm == 1 && !nextValue) return 1
    else if (bpm >= 992 && nextValue) return 999

    //makes a list that defines what values a physical metronome would have
    //of course, we support much more than a normal one so this is extended.
    val bpmSequence = sequenceOf(
        listOf(1),
        2 until 60 step 2,
        60 until 72 step 3,
        72 until 120 step 4,
        120 until 144 step 6,
        144 until 1009 step 8
    ).flatten()

    return if (nextValue) { bpmSequence.first { it > bpm } }
    else { bpmSequence.last { it < bpm } }
}

