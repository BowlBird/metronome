package com.carsonmiller.metronome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.state.PersistentMusicSegment
import com.carsonmiller.metronome.state.ScreenSettings
import com.carsonmiller.metronome.ui.theme.musicFont

@Composable
fun SettingsPage(modifier: Modifier = Modifier, musicSegmentState: PersistentMusicSegment) = ConstraintLayout(
    modifier = modifier.fillMaxSize(),
    constraintSet = settingsPageConstraint(),
) {
    Subdivision(
        modifier = Modifier
            .padding(ScreenSettings.innerPadding)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .layoutId("subdivisionSlider"),
        musicSettings = musicSegmentState)
    TapBPM(
        modifier = Modifier
            .size(ScreenSettings.buttonContainerHeight)
            .padding(ScreenSettings.innerPadding)
            .layoutId("tapBPMButton"),
        musicSettings = musicSegmentState)
    TimeSignatureControl(
        modifier = Modifier
            .layoutId("timeSignature"),
        fontSize = 120,
        numerator = musicSegmentState.numerator,
        denominator = musicSegmentState.denominator,
        musicSettings = musicSegmentState
    )
}

@Composable
private fun Subdivision(modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment) =
    TabRow(
        modifier = modifier,
        selectedTabIndex = musicSettings.subdivision - 1, //minus one because subdivision starts at 1 not 0
        backgroundColor = MaterialTheme.colorScheme.inversePrimary,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        repeat(4) {
            Button(
                modifier = Modifier
                    .height(ScreenSettings.subdivisionSliderButtonHeight),
                onClick = { musicSettings.subdivision = it + 1 },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                shape = RectangleShape,
                elevation = null
            ) {}
        }
    }

@Composable
private fun TapBPM(modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment) {
    //variable is here because it shouldn't be remembered in hard drive
    var tap: Long by remember { mutableStateOf(-1)}

    Button(modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.inversePrimary),
        elevation = null,
        onClick = {
            val currentTime = System.currentTimeMillis()
            val interval = currentTime - tap
            if(interval <= 10000)
                musicSettings.bpm = (60000 / interval).toInt() //converts to bpm (min of 6 bpm)
            tap = System.currentTimeMillis()
        }) {}
}

@Composable
private fun TimeSignatureControl(modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment,fontSize: Int, numerator: Int, denominator: Int) =
    ConstraintLayout(
        modifier = modifier,
        constraintSet = timeSignatureControlConstraint()
    ) {
        @Composable
        fun SmallButton(
            layoutId: String, contents: @Composable () -> Unit, onClick: () -> Unit
        ) = MusicButton(
            modifier = Modifier
                .size(ScreenSettings.smallButtonContainerHeight)
                .layoutId(layoutId),
            isHoldable = false,
            contents = contents,
            onClick = onClick,
        )
        SmallButton("topLeftButton", onClick = { musicSettings.numerator -= 1 }, contents = {})
        SmallButton("topRightButton", onClick = { musicSettings.numerator += 1 }, contents = {})
        SmallButton("bottomLeftButton", onClick = {
            musicSettings.denominator /= 2
            val n = musicSettings.numerator
            musicSettings.numerator = if(n != 1) 1 else 2
            musicSettings.numerator = n}, contents = {})
        SmallButton("bottomRightButton", onClick = {
            musicSettings.denominator *= 2
            val n = musicSettings.numerator
            musicSettings.numerator = if(n != 1) 1 else 2
            musicSettings.numerator = n}, contents = {})
    TimeSignature(
        modifier = modifier
            .width(100.dp)
            .height(155.dp)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .layoutId("timeSignature"),
        fontSize = fontSize,
        numerator = numerator,
        denominator = denominator
    )
}

@Composable
private fun TimeSignature(modifier: Modifier = Modifier, fontSize: Int, numerator: Int, denominator: Int) =
    ConstraintLayout(
        constraintSet = timeSignatureConstraint(fontSize),
        modifier = modifier
    ) {
        //makes the font the same size no matter system settings.
        val spFontSize = with(LocalDensity.current) { (fontSize / fontScale).sp }
        Text(
            modifier = Modifier
                .layoutId("numerator"),
            text = "$numerator",
            fontFamily = musicFont,
            fontSize = spFontSize)
        Text(
            modifier = Modifier
                .layoutId("denominator"),
            text = "$denominator",
            fontFamily = musicFont,
            fontSize = spFontSize)
    }
