package com.carsonmiller.metronome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
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
import com.carsonmiller.metronome.state.MusicSheet
import com.carsonmiller.metronome.state.ScreenSettings
import com.carsonmiller.metronome.ui.theme.musicFont

@Composable
fun SettingsPage(modifier: Modifier = Modifier, musicSheet: MusicSheet) = ConstraintLayout(
    modifier = modifier.fillMaxSize(),
    constraintSet = settingsPageConstraint(),
) {
    Subdivision(
        modifier = Modifier
            .padding(ScreenSettings.innerPadding)
            .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
            .layoutId("subdivisionSlider"),
        selectedTab = musicSheet.subdivision - 1,
        setSubdivisions = {i -> musicSheet.subdivision = i})

    //variable is here because it shouldn't be remembered in hard drive
    var tap: Long by remember { mutableStateOf(-1)}

    TapBPM(
        modifier = Modifier
            .size(ScreenSettings.buttonContainerHeight)
            .padding(ScreenSettings.innerPadding)
            .layoutId("tapBPMButton"),
        onTap = remember {{
                val currentTime = System.currentTimeMillis()
                val interval = currentTime - tap
                if(interval <= 10000)
                    musicSheet.rawBPM = (60000 / interval).toInt() //converts to bpm (min of 6 bpm)
                tap = System.currentTimeMillis()
        }})

    val decrementNumerator = remember(musicSheet) {{musicSheet.numerator -= 1}}
    val incrementNumerator = remember(musicSheet) {{musicSheet.numerator += 1}}
    val decrementDenominator = remember(musicSheet) {{
        musicSheet.denominator /= 2
        val n = musicSheet.numerator
        musicSheet.numerator = if(n != 1) 1 else 2
        musicSheet.numerator = n}}
    val incrementDenominator = remember(musicSheet) {{
        musicSheet.denominator *= 2
        val n = musicSheet.numerator
        musicSheet.numerator = if(n != 1) 1 else 2
        musicSheet.numerator = n
    }}

    TimeSignatureControl(
        modifier = Modifier
            .layoutId("timeSignature"),
        fontSize = 120,
        numerator = musicSheet.numerator,
        denominator = musicSheet.denominator,
        decrementNumerator = decrementNumerator,
        incrementNumerator = incrementNumerator,
        decrementDenominator = decrementDenominator,
        incrementDenominator = incrementDenominator
    )
}

@Composable
private fun Subdivision(modifier: Modifier = Modifier, selectedTab: Int, setSubdivisions: (i: Int) -> Unit) =
    TabRow(
        modifier = modifier,
        selectedTabIndex = selectedTab,
        backgroundColor = MaterialTheme.colorScheme.inversePrimary,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        repeat(4) {
            Button(
                modifier = Modifier
                    .height(ScreenSettings.subdivisionSliderButtonHeight),
                onClick = remember {{setSubdivisions(it + 1)}},
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                shape = RectangleShape,
                elevation = null
            ) {}
        }
    }

@Composable
private fun TapBPM(modifier: Modifier = Modifier, onTap: () -> Unit) {
    Button(modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.inversePrimary),
        elevation = null,
        onClick = remember {{onTap()}} ) {}
}

@Composable
private fun TimeSignatureControl(
    modifier: Modifier = Modifier,
    decrementNumerator: () -> Unit,
    incrementNumerator: () -> Unit,
    decrementDenominator: () -> Unit,
    incrementDenominator: () -> Unit,
    fontSize: Int,
    numerator: Int,
    denominator: Int) =
    ConstraintLayout(
        modifier = modifier,
        constraintSet = timeSignatureControlConstraint()
    ) {
        SmallButton("topLeftButton", onClick = decrementNumerator, contents = {})
        SmallButton("topRightButton", onClick = incrementNumerator, contents = {})
        SmallButton("bottomLeftButton", onClick = decrementDenominator, contents = {})
        SmallButton("bottomRightButton", onClick = incrementDenominator, contents = {})
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
private fun SmallButton(
    layoutId: String, contents: @Composable () -> Unit, onClick: () -> Unit
) = Button(
    modifier = Modifier
        .size(ScreenSettings.smallButtonContainerHeight)
        .layoutId(layoutId),
    isHoldable = false,
    contents = contents,
    onClick = onClick,
)

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
            fontSize = spFontSize,
            color = MaterialTheme.colorScheme.onBackground)
        Text(
            modifier = Modifier
                .layoutId("denominator"),
            text = "$denominator",
            fontFamily = musicFont,
            fontSize = spFontSize,
            color = MaterialTheme.colorScheme.onBackground)
    }
