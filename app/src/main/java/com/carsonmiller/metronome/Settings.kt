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
    fun clearPreferences() =
        sharedPref.edit().clear().apply()

    protected fun putInt(string: String, value: Int): Int {
        sharedPref.edit().putInt(string, value).apply()
        return value
    }

    protected fun putBoolean(string: String, value: Boolean): Boolean {
        sharedPref.edit().putBoolean(string, value).apply()
        return value
    }
}

/**
 * holder for settings
 */
class PersistentMusicSettings(activity: Activity, index: Int) : PersistentSettings(activity) {
    /* strings for sharedPref */
    private val numeratorString = "numerator$index"
    private val denominatorString = "denominator$index"
    private val bpmString = "bpm$index"

    /* backing fields */
    private var _numerator: Int by mutableStateOf(sharedPref.getInt(numeratorString, 4))
    private var _denominator: Int by mutableStateOf(sharedPref.getInt(denominatorString, 4))
    private var _bpm: Int by mutableStateOf(sharedPref.getInt(bpmString, 100))

    var numerator: Int
        get() = _numerator
        set(value) {
            _numerator = when {
                value < 1 -> putInt(numeratorString, 1)
                value > 999 -> putInt(numeratorString, 999)
                else -> putInt(numeratorString, value)
            }
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            _denominator = when {
                value < 1 -> putInt(denominatorString, 1)
                value > 64 -> putInt(denominatorString, 64)
                else -> putInt(denominatorString, value)
            }
        }

    var bpm: Int
        get() = _bpm
        set(value) {
            _bpm = when {
                value < 1 -> putInt(bpmString, 1)
                value > 999 -> putInt(bpmString, 999)
                else -> putInt(bpmString, value)
            }
        }
}

/**
 * holder that is a list so that multiple music settings can be loaded
 */
class PersistentMusicSettingsList(private val activity: Activity) : PersistentSettings(activity) {
    /* strings for sharedPref */
    private val countString = "count"

    /* backing fields */
    private var _count: Int by mutableStateOf(sharedPref.getInt(countString, 1))

    var count: Int
        get() = _count
        private set(value) {
            _count = when {
                value < 1 -> putInt(countString, 1)
                else -> putInt(countString, value)
            }
        }

    operator fun get(i: Int): PersistentMusicSettings {
        require(i < count)
        return PersistentMusicSettings(activity, i)
    }

    fun add() {
        count++
    }

    fun remove(i: Int) {
        repeat((count - 1) - i) {
            val index = it + i
            val copyTo = PersistentMusicSettings(activity, index)
            val copyFrom = PersistentMusicSettings(activity, index + 1)
            copyTo.denominator = copyFrom.denominator
            copyTo.numerator = copyFrom.numerator
            copyTo.bpm = copyFrom.bpm
        }
        val cleanMusicSettings = PersistentMusicSettings(activity, --count)
        cleanMusicSettings.bpm = 100
        cleanMusicSettings.denominator = 4
        cleanMusicSettings.numerator = 4
    }
}

/**
 * holds certain states of the app
 */
class PersistentAppSettings(activity: Activity) : PersistentSettings(activity) {
    /* strings for sharedPref */
    private val timeSignatureExpandedString = "timeSignatureExpanded"
    private val currentMusicSettingsString = "currentMusicSettings"

    /* backing fields */
    private var _timeSignatureExpanded: Boolean by mutableStateOf(
        sharedPref.getBoolean(
            timeSignatureExpandedString, false
        )
    )
    private var _currentMusicSettings: Int by mutableStateOf(
        sharedPref.getInt(currentMusicSettingsString, 0)
    )

    var timeSignatureExpanded: Boolean
        get() = _timeSignatureExpanded
        set(value) {
            _timeSignatureExpanded = putBoolean(timeSignatureExpandedString, value)
        }

    var currentMusicSettings: Int
        get() = _currentMusicSettings
        set(value) {
            _currentMusicSettings = when {
                value < 0 -> putInt(currentMusicSettingsString, 0)
                else -> putInt(currentMusicSettingsString, value)
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
