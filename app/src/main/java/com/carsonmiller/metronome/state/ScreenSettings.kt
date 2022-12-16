package com.carsonmiller.metronome.state

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/* Static Screen Setting References for the rest of the app */
class ScreenSettings {
    companion object {
        val cornerRounding: Dp = 8.dp //for rounded shapes

        /* padding */
        val containerSidePadding: Dp = 32.dp
        val containerHeightPadding: Dp = 0.dp
        val innerPadding: Dp = 10.dp //for inside containers

        /* margins */
        val containerMargins: Dp = 10.dp

        /* container heights */
        val bpmTextContainerHeight: Dp = 25.dp
        val headerContainerHeight: Dp = 130.dp
        val buttonContainerHeight: Dp = 80.dp
        val smallButtonContainerHeight: Dp = 35.dp
        val normalButtonSize = 50.dp
        val settingsContainerHeight: Dp = 370.dp
        val subdivisionSliderButtonHeight = 50.dp
        val barHeight = 5.dp

        val dotSize = 12.dp
    }
}