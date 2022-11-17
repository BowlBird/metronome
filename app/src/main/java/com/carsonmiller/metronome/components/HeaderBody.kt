package com.carsonmiller.metronome.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.carsonmiller.metronome.*
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.ui.theme.musicFont
import kotlin.math.round


@OptIn(ExperimentalMotionApi::class)
@Composable
fun HeaderBody(
    modifier: Modifier = Modifier,
    numerator: Int,
    denominator: Int,
    appSettings: PersistentAppSettings,
    musicSettings: PersistentMusicSettings
) {
    val variableFloat = round(
        animateFloatAsState(
            if(appSettings.timeSignatureExpanded) 1f else 0f,
            tween(300),
            .3f
        ).value * 100
    ) / 100

    val maxWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    MotionLayout(
        motionHeaderConstraint(maxWidth,false),
        motionHeaderConstraint(maxWidth,true),
        progress = variableFloat,
        modifier = modifier
    ) {
        TimeSignatureContainer(
            Modifier
                .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .clickable {
                    appSettings.timeSignatureExpanded = !appSettings.timeSignatureExpanded
                }
                .layoutId("timeSignatureContainer"),
            animationProgress = variableFloat,
            musicSettings = musicSettings,
            numerator = numerator,
            denominator = denominator)

        //Notes and Music Bar Holder
        MusicStaffContainer(
            modifier = Modifier
                .clip(RoundedCornerShape(ScreenSettings.cornerRounding))
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .layoutId("noteContainer"))
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun TimeSignatureContainer(
    modifier: Modifier = Modifier, animationProgress: Float, musicSettings: PersistentMusicSettings,
    numerator: Int, denominator: Int) {
    //holds and contains logic for time signature
    MotionLayout(
        motionTimeSignatureConstraint(false),
        motionTimeSignatureConstraint(true),
        progress = animationProgress,
        modifier = modifier
    ) {
        //ConstraintLayout(motionTimeSignatureConstraint(true),modifier = modifier) {
        TimeSignature(
            modifier = Modifier.layoutId("timeSignature"), numerator, denominator
        )
        val buttonColor =
            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)

        @Composable
        fun SmallButton(
            layoutId: String, contents: @Composable () -> Unit, onClick: () -> Unit
        ) = MusicButton(
            modifier = Modifier
                .size(ScreenSettings.smallButtonContainerHeight)
                .layoutId(layoutId),
            isHoldable = true,
            contents = contents,
            onClick = onClick,
            colors = buttonColor
        )
        SmallButton("topLeft", onClick = { musicSettings.numerator -= 1 }, contents = {})
        SmallButton("topRight", onClick = { musicSettings.numerator += 1 }, contents = {})
        SmallButton("bottomLeft", onClick = { musicSettings.denominator /= 2 }, contents = {})
        SmallButton("bottomRight", onClick = { musicSettings.denominator *= 2 }, contents = {})
    }
}

@Composable
private fun MusicStaffContainer(modifier: Modifier = Modifier) = Box(
    modifier = modifier) {
    //music bar (doesn't actually move)
    MusicBar(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 20.dp)//hardcoded value for alignment
    )

    //row
    HorizontalScrollContainer {
        repeat(100) {
            Note(
                modifier = Modifier
                    .height(55.dp)      //hardcoded values for alignment
                    .offset(y = 12.dp), //hardcoded values for alignment
                note = R.drawable.ic_one_hundred_twenty_eighth_note_both_connected
            )
        }
    }
}

/**
 * ToDo
 */
@Composable
private fun Note(modifier: Modifier = Modifier, note: Int) {
    Image(
        painterResource(id = note),
        modifier = modifier
            .scale(1.004f) //hardcoded values for alignment
            .wrapContentSize(),
        contentDescription = "Note",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )
}

/**
 * the music bar (basically just an image with controllable x scale)
 */
@Composable
private fun MusicBar(modifier: Modifier = Modifier) =
    Image(
        painterResource(id = R.drawable.ic_music_staff),
        modifier = modifier.scale(100f , 1f), //100 just so it's long enough you'll never see the end of it.
        contentDescription = "Music Staff",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
    )

/**
 * Time signature with controllable numerator and denominator
 */
@Composable
private fun TimeSignature(
    modifier: Modifier = Modifier, numerator: Int, denominator: Int, fontSize: Int = 70,
    color: Color = MaterialTheme.colorScheme.onBackground) {
    //makes the font the same size no matter system settings.
    val spFontSize = with(LocalDensity.current) { (fontSize / fontScale).sp }

    //creates the two numbers
    ConstraintLayout(
        timeSignatureConstraint(fontSize), modifier = modifier) {
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

/**
 * Inner time signature number that is just a wrapper for text
 */
@Composable
private fun TimeSignatureNumber(
    modifier: Modifier = Modifier, value: Int, fontSize: TextUnit, color: Color) =
    Text(
        text = value.toString(),
        modifier = modifier,
        fontFamily = musicFont,
        fontSize = fontSize,
        color = color
    )
