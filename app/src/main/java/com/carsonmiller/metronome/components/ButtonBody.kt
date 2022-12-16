package com.carsonmiller.metronome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.carsonmiller.metronome.state.ScreenSettings

@Composable
fun ButtonBody(
    modifier: Modifier = Modifier,
    decrementRounded: () -> Unit,
    decrement: () -> Unit,
    togglePlay: () -> Unit,
    increment: () -> Unit,
    incrementRounded: () -> Unit,
    height: Dp) =
    RowContainer(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        height = height,
        fillMaxWidth = false,
    ) {
        Button(size = ScreenSettings.normalButtonSize, onClick = decrementRounded, contents = { }, isHoldable = true)
        Button(size = ScreenSettings.normalButtonSize, onClick = decrement, contents = {  }, isHoldable = true)
        Button(size = ScreenSettings.normalButtonSize * 1.2f, onClick = togglePlay, contents = {  }, isHoldable = false)
        Button(size = ScreenSettings.normalButtonSize, onClick = increment, contents = {  }, isHoldable = true)
        Button(size = ScreenSettings.normalButtonSize, onClick = incrementRounded, contents = {}, isHoldable = true)
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

@Composable
private fun Button(
    modifier: Modifier = Modifier,
    size: Dp, onClick: () -> Unit,
    contents: @Composable () -> Unit,
    isHoldable: Boolean) =
    Button(
        modifier
            .padding(ScreenSettings.innerPadding / 2) //since these objects are right next to each other it would be 20 otherwise
            .size(size),
        onClick = onClick,
        contents = contents,
        isHoldable = isHoldable
    )
