package com.carsonmiller.metronome

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.components.*
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import kotlinx.parcelize.RawValue


class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
    private var _bpm: Int by mutableStateOf(sharedPref.getInt(bpmString, 100))

    var numerator: Int
        get() = _numerator
        set(value) {
            _numerator = when {
                value < 1 -> put(numeratorString, 1)
                value > 999 -> put(numeratorString, 999)
                else -> put(numeratorString, value)
            }
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            _denominator = when {
                value < 1 -> put(denominatorString, 1)
                value > 999 -> put(denominatorString, 999)
                else -> put(denominatorString, value)
            }
        }

    var bpm: Int
        get() = _bpm
        set(value) {
            _bpm = when {
                value < 1 -> put(bpmString, 1)
                value > 999 -> put(bpmString, 999)
                else -> put(bpmString, value)
            }
        }

    private fun put(string: String, value: Int): Int {
        sharedPref.edit().putInt(string, value).apply()
        return value
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
) {
    //text for bpm
    BpmTextBody(
        modifier = Modifier
            .wrapContentSize()
            .layoutId("bpmText"), bpm = settings.bpm
    )

    //Music staff container
    HeaderBody(
        modifier = Modifier
            .containerModifier(ScreenSettings().scrollContainerHeight)
            .layoutId("scrollBox"),
        numerator = settings.numerator,
        denominator = settings.denominator
    )

    //Button container
    ButtonBody(
        modifier = Modifier
            .containerModifier(ScreenSettings().buttonContainerHeight)
            .layoutId("buttonBox"),
        settings = settings //only pass in settings when state is being changed.
    )

    //settings container
    PagerContainer(modifier = Modifier
        .containerModifier(ScreenSettings().settingsContainerHeight)
        .layoutId("settingsBox"), { Text("Test") }, { Text("Test2") }, { Text("Test3") })
}