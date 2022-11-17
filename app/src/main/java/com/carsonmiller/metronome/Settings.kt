package com.carsonmiller.metronome

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * abstract class for extending
 */
abstract class PersistentSettings(activity: Activity) {
    protected val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    protected fun put(string: String, value: Int): Int {
        sharedPref.edit().putInt(string, value).apply()
        return value
    }
}

/**
 * holder for settings
 */
class PersistentMusicSettings(activity: Activity) : PersistentSettings(activity) {
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
                value > 64 -> put(denominatorString, 64)
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
}

/* Static Screen Setting References for the rest of the app */
class ScreenSettings {
    companion object {
        val cornerRounding: Dp = 10.dp //for rounded shapes

        /* padding */
        val containerSidePadding: Dp = 32.dp
        val containerHeightPadding: Dp = 0.dp
        val innerPadding: Dp = 10.dp //for inside containers

        /* margins */
        val containerMargins: Dp = 20.dp

        /* container heights */
        val headerContainerHeight: Dp = 100.dp
        val buttonContainerHeight: Dp = 80.dp
        val smallButtonContainerHeight: Dp = 25.dp
        val settingsContainerHeight: Dp = 400.dp
    }
}

/**
 * holds certain states of the app
 */
class PersistentAppSettings(activity: Activity) : PersistentSettings(activity) {
    /* strings for sharedPref */
    private val timeSignatureExpandedString = "timeSignatureExpanded"

    /* backing fields */
    private var _timeSignatureExpanded: Boolean by mutableStateOf(
        sharedPref.getBoolean(
            timeSignatureExpandedString, false
        )
    )

    var timeSignatureExpanded: Boolean
        get() = _timeSignatureExpanded
        set(value) {
            _timeSignatureExpanded = value
        }
}
