package com.carsonmiller.metronome.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.carsonmiller.metronome.state.*


@OptIn(ExperimentalMotionApi::class)
@Composable
fun HeaderBody(
    modifier: Modifier = Modifier,
    numerator: Int,
    denominator: Int,
    appSettings: PersistentAppSettings,
    musicSettings: PersistentMusicSegment
) {
    val variableFloat = round(
        animateFloatAsState(
            if (appSettings.timeSignatureExpanded) 1f else 0f, tween(300), .3f
        ).value * 100
    ) / 100

    val maxWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    MotionLayout(
        remember { motionHeaderConstraint(maxWidth, false) },
        remember { motionHeaderConstraint(maxWidth, true) },
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
                .layoutId("noteContainer"), musicSettings = musicSettings
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun TimeSignatureContainer(
    modifier: Modifier = Modifier,
    animationProgress: Float,
    musicSettings: PersistentMusicSegment,
    numerator: Int,
    denominator: Int
) {
    //holds and contains logic for time signature
    MotionLayout(
        remember { motionTimeSignatureConstraint(false) },
        remember { motionTimeSignatureConstraint(true) },
        progress = animationProgress,
        modifier = modifier
    ) {
        TimeSignature(
            modifier = Modifier
                .offset(y = 12.dp)
                .layoutId("timeSignature"),
            numerator = numerator,
            denominator = denominator,
            fontSize = 85
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
private fun MusicStaffContainer(
    modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment
) = Box(
    modifier = modifier
) {
    //music bar (doesn't actually move)
    MusicBar(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 20.dp)//hardcoded value for alignment
    )
    //row
    HorizontalScrollContainer(
        modifier = Modifier.fillMaxHeight()

    ) {
        Contents(musicSettings = musicSettings)
    }

}

@Composable
private fun Contents(modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment) =
    Box(modifier) {
        if(musicSettings.subdivision == 3) {
            Row(Modifier.offset(x = (1).dp, y = (-58).dp)) {
                repeat(musicSettings.numOfNotes / 3) {
                    Image(
                        painterResource(id = R.drawable.ic_triplet_indicator),
                        modifier = modifier
                            .size((55 * 3).dp),
                        contentDescription = "triplet indicator",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
        Row(Modifier) {
            Notes(
                modifier = Modifier
                    .offset(y = 20.dp),
                musicSettings = musicSettings
            )
            //I want to be able to scroll just a little further, so add an extra little space
            Box(modifier.width(25.dp)) {}
        }
    }


@Composable
private fun Notes(modifier: Modifier = Modifier, musicSettings: PersistentMusicSegment) {
    for (noteNum in 0 until musicSettings.numOfNotes) {
        Note(
            modifier = modifier
                .height(55.dp)
                .offset(y = (12).dp), note = musicSettings[noteNum]
        )
    }
}

/**
 * Represents and holds note value
 */
@Composable
private fun Note(modifier: Modifier = Modifier, note: PersistentNote) {
    Column {
        Image(
            painterResource(id = note.noteImage),
            modifier = modifier
                .scale(1.004f) //hardcoded values for alignment
                .clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null
                ) {
                    note.level = when (note.level) {
                        NoteIntensity.Rest -> NoteIntensity.Quiet
                        NoteIntensity.Quiet -> NoteIntensity.Normal
                        NoteIntensity.Normal -> NoteIntensity.Loud
                        NoteIntensity.Loud -> NoteIntensity.Rest
                    }
                },
            contentDescription = "Note",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )

        val image = when (note.level) {
            NoteIntensity.Loud -> R.drawable.ic_accent
            NoteIntensity.Quiet -> R.drawable.ic_soft
            else -> R.drawable.ic_blank
        }
        Image(
            painterResource(image),
            modifier = modifier
                .scale(0.9f)
                .offset(y = (-16).dp),
            contentDescription = "Accent",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

/**
 * the music bar (basically just an image with controllable x scale)
 */
@Composable
private fun MusicBar(modifier: Modifier = Modifier) = Image(
    painterResource(id = R.drawable.ic_music_staff),
    modifier = modifier.scale(
        100f, 1f
    ), //100 just so it's long enough you'll never see the end of it.
    contentDescription = "Music Staff",
    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
)

/**
 * Time signature with controllable numerator and denominator
 */
@Composable
private fun TimeSignature(
    modifier: Modifier = Modifier,
    numerator: Int,
    denominator: Int,
    fontSize: Int = 70,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    //makes the font the same size no matter system settings.
    val spFontSize = with(LocalDensity.current) { (fontSize / fontScale).sp }

    //creates the two numbers
    ConstraintLayout(
        timeSignatureConstraint(fontSize), modifier = modifier
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

/**
 * Inner time signature number that is just a wrapper for text
 */
@Composable
private fun TimeSignatureNumber(
    modifier: Modifier = Modifier, value: Int, fontSize: TextUnit, color: Color
) = Text(
    text = value.toString(),
    modifier = modifier,
    fontFamily = musicFont,
    fontSize = fontSize,
    color = color
)
