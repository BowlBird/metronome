package com.carsonmiller.metronome.components

import com.carsonmiller.metronome.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
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
fun PagerContainer(
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean,
    height: Dp,
    vararg page: @Composable () -> Unit) =
    HorizontalPager(
        modifier =
            if(fillMaxWidth)
                modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(
                        ScreenSettings.containerSidePadding,
                        ScreenSettings.containerHeightPadding
                    )
                    .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            else
                modifier
                    .height(height)
                    .padding(
                        ScreenSettings.containerSidePadding,
                        ScreenSettings.containerHeightPadding
                    )
                    .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                    .background(color = MaterialTheme.colorScheme.primaryContainer),
        count = page.size,
        state = rememberPagerState()
    ) {
        page[it].invoke()
    }

@Composable
fun Button(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit,
    isHoldable: Boolean = false,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.inversePrimary),
) {
    val interaction = remember { MutableInteractionSource() }
    Button(
        modifier = modifier,
        onClick = { } ,
        shape = remember {CircleShape},
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

@Composable
fun ConstraintContainer(
    modifier: Modifier = Modifier,
    constraintSet: ConstraintSet = ConstraintSet {},
    height: Dp,
    fillMaxWidth: Boolean,
    contents: @Composable () -> Unit) =
        ConstraintLayout(
            modifier =
                if(fillMaxWidth)
                    modifier
                        .fillMaxWidth()
                        .height(height)
                        .padding(
                            ScreenSettings.containerSidePadding,
                            ScreenSettings.containerHeightPadding
                        )
                        .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                else
                    modifier
                        .height(height)
                        .padding(
                            ScreenSettings.containerSidePadding,
                            ScreenSettings.containerHeightPadding
                        )
                        .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
            constraintSet = constraintSet,
            content = contents
        )

@Composable
fun BoxContainer(
    modifier: Modifier = Modifier,
    height: Dp,
    fillMaxWidth: Boolean,
    contents: @Composable () -> Unit) =
    Box(
        modifier =
        if(fillMaxWidth)
            modifier
                .fillMaxWidth()
                .height(height)
                .padding(ScreenSettings.containerSidePadding, ScreenSettings.containerHeightPadding)
                .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        else
            modifier
                .height(height)
                .padding(ScreenSettings.containerSidePadding, ScreenSettings.containerHeightPadding)
                .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                .background(color = MaterialTheme.colorScheme.primaryContainer),

    ) {
        contents()
    }

@Composable
fun RowContainer(
    modifier: Modifier = Modifier,
    height: Dp,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    fillMaxWidth: Boolean,
    contents: @Composable () -> Unit) =
    Row(
        modifier =
    if(fillMaxWidth)
        modifier
            .fillMaxWidth()
            .height(height)
            .padding(ScreenSettings.containerSidePadding, ScreenSettings.containerHeightPadding)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    else
        modifier
            .height(height)
            .padding(ScreenSettings.containerSidePadding, ScreenSettings.containerHeightPadding)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment)
    {
        contents()
    }


