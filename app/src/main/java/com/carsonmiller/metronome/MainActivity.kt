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
    modifier = Modifier
        .fillMaxSize()
        .background(color = colorScheme.background)
)
{

    //refs for each composable held in constraint layout
    val (scrollBox, buttonBox, settingsBox, bpmText) = createRefs()

    //text for bpm
    BpmText(
        modifier = Modifier
            .wrapContentSize()
            .constrainAs(bpmText) {
                end.linkTo(
                    parent.end,
                    margin = ScreenSettings().containerSidePadding * 2
                ) //*2 for some contrast
                bottom.linkTo(
                    scrollBox.top,
                    margin = ScreenSettings().containerMargins / 2 // / 2 to make it closer
                )
            }
    )

    //Music staff container
    ScrollableStaffContents(
        modifier = Modifier
            .containerModifier(ScreenSettings().scrollHeight)
            .constrainAs(scrollBox) {
                bottom.linkTo(buttonBox.top, margin = ScreenSettings().containerMargins)
            }
    )

    //Button container
    ButtonContainer(
        modifier = Modifier
            .containerModifier(ScreenSettings().buttonHeight)
            .constrainAs(buttonBox) {
                centerVerticallyTo(parent, .4f)
            }
    )

    //settings container
    PagerContainer(
        modifier = Modifier
            .containerModifier(ScreenSettings().settingsHeight)
            .constrainAs(settingsBox) {
                top.linkTo(buttonBox.bottom, margin = ScreenSettings().containerMargins)
            },
        { Text("Test") },
        { Text("Test2") },
        { Text("Test3") }
    )
}

/**
 * Text that shows current bpm
 */
@Composable
fun BpmText(modifier: Modifier, bpm: Int = 100) {
    ConstraintLayout(
        modifier = modifier
    ) {

        val (num, bpmText) = createRefs()

        //number
        Text(
            text = "$bpm",
            modifier = Modifier
                .constrainAs(num) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            color = colorScheme.onBackground,
            style = typography.labelLarge
        )

        //bpm text
        Text(
            text = "bpm",
            modifier = Modifier
                .constrainAs(bpmText) {
                    start.linkTo(num.end, margin = 5.dp)
                },
            color = colorScheme.secondary,
            style = typography.labelLarge,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun ScrollableStaffContents(modifier: Modifier) {
    val settings: MutableState<MusicSettings> =
        rememberSaveable { mutableStateOf(MusicSettings(4, 4)) }
    val padding = 10.dp

    //box to hold everything in one container
    Box(modifier = modifier) {

        //time signature container
        Box(
            modifier = Modifier
                .padding(padding)
                .clip(RoundedCornerShape(10.dp))
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
                .padding(70.dp, padding, padding, padding)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            //music bar (doesn't actually move)
            MusicBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, (20).dp),
                4f
            )

            //row
            HorizontalScrollContainer(
                modifier = Modifier.padding(13.dp)
            ) {
                //notes
                repeat(100) {
                    Note(
                        modifier = Modifier,
                        R.drawable.ic_one_hundred_twenty_eighth_note_both_connected
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
fun Note(modifier: Modifier, note: Int) {
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
        modifier = modifier
            .wrapContentSize()
    )
    {
        val (topNum, bottomNum) = createRefs()
        TimeSignatureNumber(
            modifier = Modifier.constrainAs(topNum) { top.linkTo(parent.top) },
            value = numerator,
            fontSize = spFontSize,
            color = color
        ) //numerator
        TimeSignatureNumber(
            modifier = Modifier.constrainAs(bottomNum) { top.linkTo(topNum.top, margin = 35.dp) },
            value = denominator, fontSize = spFontSize,
            color = color
        ) //denominator
    }
}