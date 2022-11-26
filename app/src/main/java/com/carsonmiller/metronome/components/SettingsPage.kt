package com.carsonmiller.metronome.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.state.PersistentMusicSegment
import com.carsonmiller.metronome.state.ScreenSettings

@Composable
fun SettingsPage(modifier: Modifier = Modifier, musicSegmentState: PersistentMusicSegment) = ConstraintLayout(
    settingsPageConstraint(),
    modifier = modifier
) {
    TabRow(
        modifier = Modifier
            .padding(ScreenSettings.innerPadding)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .layoutId("subdivisionSlider"),
        selectedTabIndex = musicSegmentState.subdivision - 1, //minus one because subdivision starts at 1 not 0
        backgroundColor = MaterialTheme.colorScheme.inversePrimary,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        repeat(4) {
            Button(
                modifier = Modifier
                    .height(ScreenSettings.subdivisionSliderButtonHeight),
                onClick = { musicSegmentState.subdivision = it + 1 },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                shape = RectangleShape,
                elevation = null
            ) {}
        }
    }
}