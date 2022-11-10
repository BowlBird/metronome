package com.carsonmiller.metronome

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.ui.theme.musicFont
import com.carsonmiller.metronome.ui.theme.typography
import kotlinx.parcelize.RawValue


class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val settings: MusicSettingsViewModel by viewModels()
            val settings = PersistentMusicSettings(this)
            MetronomeTheme { MainLayout(settings) }
        }
    }
}

/**
 * holder for settings
 */
class PersistentMusicSettings(activity: Activity) {
    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

    /* strings for sharedPref */
    private val numeratorString = "numerator"
    private val denominatorString = "denominator"
    private val bpmString = "bpm"

    /* backing fields */
    private var _numerator: Int by mutableStateOf(sharedPref.getInt(numeratorString, 4))
    private var _denominator: Int by mutableStateOf(sharedPref.getInt(denominatorString, 4))
    private var _bpm: Int by mutableStateOf(sharedPref.getInt(bpmString, 4))

    var numerator: Int
        get() = _numerator
        set(value) {
            sharedPref.edit().putInt(numeratorString, value).apply()
            _numerator = value
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            sharedPref.edit().putInt(denominatorString, value).apply()
            _denominator = value
        }

    var bpm: Int
        get() = _bpm
        set(value) {
            sharedPref.edit().putInt(bpmString, value).apply()
            _bpm = value
        }
}

/**
 * Ideally this will just be a bunch of constants functions can use to not bloat parameter lists.
 */
data class ScreenSettings(
    val cornerRounding: @RawValue Dp = 10.dp, //for rounded shapes
    /* padding */
    val containerSidePadding: @RawValue Dp = 32.dp,
    val containerHeightPadding: @RawValue Dp = 0.dp,
    val innerPadding: @RawValue Dp = 10.dp, //for inside containers
    /* margins */
    val containerMargins: @RawValue Dp = 20.dp,
    /* container heights */
    val scrollContainerHeight: @RawValue Dp = 100.dp,
    val buttonContainerHeight: @RawValue Dp = 80.dp,
    val settingsContainerHeight: @RawValue Dp = 400.dp
)

@Composable
fun MainLayout(settings: PersistentMusicSettings) = ConstraintLayout(
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
            .layoutId("bpmText"),
        bpm = settings.bpm
    )

    //Music staff container
    ScrollableStaffContents(
        modifier = Modifier
            .containerModifier(ScreenSettings().scrollContainerHeight)
            .layoutId("scrollBox"),
        settings = settings
    )

    //Button container
    ButtonContents(
        modifier = Modifier
            .containerModifier(ScreenSettings().buttonContainerHeight)
            .layoutId("buttonBox"),
        settings = settings
    )

    //settings container
    PagerContainer(
        modifier = Modifier
            .containerModifier(ScreenSettings().settingsContainerHeight)
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
fun ScrollableStaffContents(modifier: Modifier = Modifier, settings: PersistentMusicSettings) {

    //box to hold everything in one container
    Box(modifier = modifier) {
        //time signature container
        Box(
            modifier = Modifier
                .padding(ScreenSettings().innerPadding)
                .clip(RoundedCornerShape(ScreenSettings().cornerRounding))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .width(55.dp),
            contentAlignment = Alignment.Center
        ) {
            TimeSignature(
                modifier = Modifier,
                settings.numerator,
                settings.denominator
            )
        }

        //Notes and Music Bar Holder
        Box(
            modifier = Modifier
                .padding(
                    75.dp,
                    ScreenSettings().innerPadding,
                    ScreenSettings().innerPadding,
                    ScreenSettings().innerPadding
                )
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

@Composable
fun ButtonContents(modifier: Modifier = Modifier, settings: PersistentMusicSettings) =
    Row(
        modifier = modifier.padding(5.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val buttonSize = 50.dp
        fun buttonModifier(size: Dp) = Modifier
            .align(Alignment.CenterVertically)
            .padding(ScreenSettings().innerPadding / 2) //since these objects are right next to eachother it would be 20 otherwise
            .size(size)
        MusicButton(
            modifier = buttonModifier(buttonSize),
            onClick = {
                settings.bpm -= 4
            }, contents = {}
        )
        MusicButton(
            modifier = buttonModifier(buttonSize),
            onClick = {
                settings.bpm -= 1
            }, contents = {}
        )
        MusicButton(
            modifier = buttonModifier(buttonSize * 1.2f),
            onClick = {
                settings.numerator -= 0
            }, contents = {}
        )
        MusicButton(
            modifier = buttonModifier(buttonSize),
            onClick = {
                settings.bpm += 1
            }, contents = {}
        )
        MusicButton(
            modifier = buttonModifier(buttonSize),
            onClick = {
                settings.bpm += 4
            }, contents = {}
        )
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