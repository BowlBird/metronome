package com.carsonmiller.metronome.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import com.carsonmiller.metronome.state.ScreenSettings
import com.carsonmiller.metronome.ui.theme.typography

/**
 * Text that shows current bpm
 */
@Composable
fun BpmTextBody(modifier: Modifier = Modifier, bpm: Int, height: Dp) {
    ConstraintContainer(
        modifier = modifier,
        constraintSet = textConstraints(),
        height = height,
        fillMaxWidth = false
    ) {
        //number
        Text(
            modifier = Modifier
                .padding(start = ScreenSettings.innerPadding)
                .layoutId("num"),
            text = "$bpm",
            color = MaterialTheme.colorScheme.onBackground,
            style = typography.labelLarge
        )

        //bpm text
        Text(
            modifier = Modifier
                .padding(end = ScreenSettings.innerPadding)
                .layoutId("bpmText"),
            text = "bpm",
            color = MaterialTheme.colorScheme.secondary,
            style = typography.labelLarge,
            fontStyle = FontStyle.Italic
        )
    }
}