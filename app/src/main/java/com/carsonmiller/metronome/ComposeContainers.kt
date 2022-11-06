package com.carsonmiller.metronome

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

/**
 * A container that pages
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerContainer(modifier: Modifier = Modifier, vararg page: @Composable () -> Unit) =
    HorizontalPager(
        modifier = modifier,
        count = page.size,
        state = rememberPagerState()
    ) {
        page[it].invoke()
    }

@Composable
fun HorizontalScrollContainer(modifier: Modifier = Modifier, contents: @Composable () -> Unit) =
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {contents()}


/**
 * extension factory function for containers
 */
fun Modifier.containerModifier(height: Dp = 0.dp) = composed {
    val screenSettings = ScreenSettings()
    this
        .fillMaxWidth()
        .height(height)
        .padding(screenSettings.containerSidePadding, screenSettings.containerHeightPadding)
        .clip(RoundedCornerShape(screenSettings.cornerRounding))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
}