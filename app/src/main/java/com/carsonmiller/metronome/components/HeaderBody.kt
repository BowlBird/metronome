package com.carsonmiller.metronome.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.*
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.ui.theme.musicFont


@Composable
fun HeaderBody(
    modifier: Modifier = Modifier,
    numerator: Int,
    denominator: Int,
    appSettings: PersistentAppState,
    musicSettings: PersistentMusicSettings
) {
    val timeSignatureContainerWidth = 80.dp
    val expandedSize = 75.dp
    val usedWidth =
        timeSignatureContainerWidth + if (appSettings.timeSignatureExpanded) expandedSize else 0.dp

    Box(modifier = modifier) {
        //holds and contains logic for time signature
        ConstraintLayout(
            timeSignatureContainerConstraint(),
            modifier = Modifier
                .padding(ScreenSettings().innerPadding)
                .clip(RoundedCornerShape(ScreenSettings().cornerRounding))
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .fillMaxHeight()
                .width(usedWidth)
                .clickable {
                    appSettings.timeSignatureExpanded = !appSettings.timeSignatureExpanded
                }
                .animateContentSize(),
        ) {
            if (appSettings.timeSignatureExpanded) {
                val buttonColor =
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)

                @Composable
                fun SmallButton(
                    layoutId: String,
                    contents: @Composable () -> Unit,
                    onClick: () -> Unit
                ) =
                    MusicButton(
                        modifier = Modifier
                            .size(ScreenSettings().smallButtonContainerHeight)
                            .layoutId(layoutId),
                        isHoldable = true,
                        contents = contents,
                        onClick = onClick,
                        colors = buttonColor
                    )
                SmallButton("topLeft", onClick = { musicSettings.numerator -= 1 }, contents = {})
                SmallButton("topRight", onClick = { musicSettings.numerator += 1 }, contents = {})
                SmallButton(
                    "bottomLeft",
                    onClick = { musicSettings.denominator /= 2 },
                    contents = {})
                SmallButton(
                    "bottomRight",
                    onClick = { musicSettings.denominator *= 2 },
                    contents = {})
            }
            TimeSignature(
                modifier = Modifier.layoutId("timeSignature"), numerator, denominator
            )

        }

        //Notes and Music Bar Holder
        Box(
            modifier = Modifier
                .padding(
                    ScreenSettings().innerPadding * 2 + usedWidth,
                    ScreenSettings().innerPadding,
                    ScreenSettings().innerPadding,
                    ScreenSettings().innerPadding
                )
                //.animateContentSize(animationSpec = TweenSpec())
                .clip(RoundedCornerShape(ScreenSettings().cornerRounding))
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            //music bar (doesn't actually move)
            MusicBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 20.dp), 4f
            )

            //row
            HorizontalScrollContainer {
                repeat(100) {
                    Note(
                        modifier = Modifier
                            .height(55.dp)
                            .offset(y = 12.dp),
                        note = R.drawable.ic_one_hundred_twenty_eighth_note_both_connected
                    )
                }
            }
        }
    }
}

/**
 * ToDo
 */
@Composable
fun Note(modifier: Modifier = Modifier, note: Int) {
    Image(
        painterResource(id = note),
        modifier = modifier
            .scale(1.004f)
            .wrapContentSize(),
        contentDescription = "Note",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )
}

/**
 * the music bar (basically just an image with controllable x scale)
 */
@Composable
fun MusicBar(modifier: Modifier = Modifier, scale: Float) {
    Image(
        painterResource(id = R.drawable.ic_music_staff),
        modifier = modifier.scale(scale, 1f),
        contentDescription = "Music Staff",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )
}

/**
 * Time signature with controllable numerator and denominator
 */
@Composable
fun TimeSignature(
    modifier: Modifier = Modifier,
    numerator: Int = 4,
    denominator: Int = 4,
    fontSize: Int = 70,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    /**
     * Inner time signature number that is just a wrapper for text
     */
    @Composable
    fun TimeSignatureNumber(
        modifier: Modifier = Modifier, value: Int, fontSize: TextUnit, color: Color
    ) {
        require(value > 0) //will throw an exception since time signatures can't be negative or 0
        Text(
            text = value.toString(),
            modifier = modifier,
            fontFamily = musicFont,
            fontSize = fontSize,
            color = color
        )
    }

    val spFontSize = with(LocalDensity.current) { //determines the size based on fontSize variable.
        (fontSize / fontScale).sp
    }

    ConstraintLayout(
        timeSignatureConstraint(fontSize), modifier = modifier.wrapContentSize()
    ) {
        TimeSignatureNumber(
            modifier = Modifier.layoutId("topText"),
            value = numerator,
            fontSize = spFontSize,
            color = color
        ) //numerator
        TimeSignatureNumber(
            modifier = Modifier.layoutId("bottomText"),
            value = denominator,
            fontSize = spFontSize,
            color = color
        ) //denominator
    }
}
