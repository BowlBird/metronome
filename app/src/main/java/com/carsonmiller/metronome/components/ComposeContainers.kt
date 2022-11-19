package com.carsonmiller.metronome.components

import com.carsonmiller.metronome.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carsonmiller.metronome.state.ScreenSettings
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.*

/**
 * A container that pages
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerContainer(modifier: Modifier = Modifier, vararg page: @Composable () -> Unit) =
    HorizontalPager(
        modifier = modifier, count = page.size, state = rememberPagerState()
    ) {
        page[it].invoke()
    }

@Composable
fun HorizontalScrollContainer(modifier: Modifier = Modifier, contents: @Composable () -> Unit) =
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) { contents() }

@Composable
fun MusicButton(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit,
    isHoldable: Boolean = false,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
) {
    val interaction = remember { MutableInteractionSource() }
    Button(
        modifier = modifier,
        onClick = {},
        shape = CircleShape,
        colors = colors,
        interactionSource = interaction
    ) {
        val delayUntilHold = 500L
        val delay = 100L
        /** gets if it is being held */
        LaunchedEffect(interaction) {
            var doButtonAction: Job? = null
            var isHeld = false

            interaction.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> if (isHoldable) {
                        doButtonAction = launch {
                            isHeld = false
                            delay(delayUntilHold)
                            while (true) {
                                isHeld = true
                                onClick()
                                delay(delay)
                            }
                        }
                    }
                    is PressInteraction.Release -> {
                        doButtonAction?.cancel()
                        if (!isHeld) onClick()
                    }
                    is PressInteraction.Cancel -> {
                        doButtonAction?.cancel()
                        if (!isHeld) onClick()
                    }
                }

            }
        }
        contents()
    }
}

/**
 * extension factory function for containers
 */
fun Modifier.containerModifier(height: Dp = 0.dp) = composed {
    this
        .wrapContentWidth(Alignment.CenterHorizontally)
        .height(height)
        .padding(ScreenSettings.containerSidePadding, ScreenSettings.containerHeightPadding)
        .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
}