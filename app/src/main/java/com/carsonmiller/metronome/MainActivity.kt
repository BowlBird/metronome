package com.carsonmiller.metronome

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.ui.theme.musicFont
import com.carsonmiller.metronome.ui.theme.typography
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MetronomeTheme { MainLayout() } }
    }
}

/**
 * holder for settings
 */
@Parcelize
data class MusicSettings(val numerator: Int = 4, val denominator: Int = 4, val bpm: Int = 100) :
    Parcelable

/**
 * Ideally this will just be a bunch of constants functions can use to not bloat parameter lists.
 */
@Parcelize
data class ScreenSettings(
    val cornerRounding: @RawValue Dp = 10.dp, //for rounded shapes
    /* padding */
    val containerSidePadding: @RawValue Dp = 32.dp,
    val containerHeightPadding: @RawValue Dp = 0.dp,
    val innerPadding: @RawValue Dp = 10.dp, //for inside containers
    /* margins */
    val containerMargins: @RawValue Dp = 20.dp,
    /* container heights */
    val scrollHeight: @RawValue Dp = 100.dp,
    val buttonHeight: @RawValue Dp = 80.dp,
    val settingsHeight: @RawValue Dp = 400.dp
) : Parcelable

@Preview
@Composable
fun MainLayout() = ConstraintLayout(
    containerConstraints(),
    modifier = Modifier
        .fillMaxSize()
        .background(color = colorScheme.background)
)
{
    //text for bpm
    BpmText(
        modifier = Modifier
            .wrapContentSize()
            .layoutId("bpmText")
    )

    //Music staff container
    ScrollableStaffContents(
        modifier = Modifier
            .containerModifier(ScreenSettings().scrollHeight)
            .layoutId("scrollBox")
    )

    //Button container
    Box(
        modifier = Modifier
            .containerModifier(ScreenSettings().buttonHeight)
            .layoutId("buttonBox")
    )

    //settings container
    PagerContainer(
        modifier = Modifier
            .containerModifier(ScreenSettings().settingsHeight)
            .layoutId("settingsBox"),
        { Text("Test") },
        { Text("Test2") },
        { Text("Test3") }
    )
}

/**
 * Text that shows current bpm
 */
@Composable
fun BpmText(modifier: Modifier = Modifier, bpm: Int = 100) {
    ConstraintLayout(
        textConstraints(),
        modifier = modifier
    ) {
        //number
        Text(
            modifier = Modifier.layoutId("num"),
            text = "$bpm",
            color = colorScheme.onBackground,
            style = typography.labelLarge
        )

        //bpm text
        Text(
            modifier = Modifier.layoutId("bpmText"),
            text = "bpm",
            color = colorScheme.secondary,
            style = typography.labelLarge,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun ScrollableStaffContents(modifier: Modifier = Modifier) {
    val settings: MutableState<MusicSettings> =
        rememberSaveable { mutableStateOf(MusicSettings(4, 4)) }

    //box to hold everything in one container
    Box(modifier = modifier) {
        //time signature container
        Box(
            modifier = Modifier
                .padding(ScreenSettings().innerPadding)
                .clip(RoundedCornerShape(ScreenSettings().cornerRounding))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            TimeSignature(
                modifier = Modifier,
                settings.component1().numerator,
                settings.component1().denominator
            )
        }

        //Notes and Music Bar Holder
        Box(
            modifier = Modifier
                .padding(70.dp, ScreenSettings().innerPadding, ScreenSettings().innerPadding, ScreenSettings().innerPadding)
                .clip(RoundedCornerShape(ScreenSettings().cornerRounding))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            //music bar (doesn't actually move)
            MusicBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 20.dp),
                4f
            )

            //row
            HorizontalScrollContainer {
                repeat(100) {
                    Note(
                        modifier = Modifier.height(55.dp).offset(y = 12.dp),
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
        colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
    )
}

/**
 * the music bar (basically just an image with controllable x scale)
 */
@Composable
fun MusicBar(modifier: Modifier = Modifier, scale: Float) {
    Image(
        painterResource(id = R.drawable.ic_music_staff),
        modifier = modifier
            .scale(scale, 1f),
        contentDescription = "Music Staff",
        colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
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
    color: Color = colorScheme.onBackground
) {
    /**
     * Inner time signature number that is just a wrapper for text
     */
    @Composable
    fun TimeSignatureNumber(
        modifier: Modifier = Modifier,
        value: Int,
        fontSize: TextUnit,
        color: Color
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
        bpmConstraints(),
        modifier = modifier
            .wrapContentSize()
    )
    {
        TimeSignatureNumber(
            modifier = Modifier.layoutId("topText"),
            value = numerator,
            fontSize = spFontSize,
            color = color
        ) //numerator
        TimeSignatureNumber(
            modifier = Modifier.layoutId("bottomText"),
            value = denominator, fontSize = spFontSize,
            color = color
        ) //denominator
    }
}