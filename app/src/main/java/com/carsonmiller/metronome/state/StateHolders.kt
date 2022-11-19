package com.carsonmiller.metronome.state

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.carsonmiller.metronome.R

enum class NoteIntensity {
    Rest, Quiet, Normal, Loud
}

enum class NoteType {
    WholeNote {override val drawable = R.drawable.ic_whole_note},
    HalfNote {override val drawable = R.drawable.ic_half_note},
    QuarterNote {override val drawable = R.drawable.ic_quarter_note},
    EighthNoteBackConnected {override val drawable = R.drawable.ic_eighth_note_back_connected},
    EighthNoteFrontConnected {override val drawable = R.drawable.ic_eighth_note_front_connected},
    EighthNoteBothConnected {override val drawable = R.drawable.ic_eighth_note_both_connected},
    SixteenthNoteBackConnected {override val drawable = R.drawable.ic_sixteenth_note_back_connected},
    SixteenthNoteFrontConnected {override val drawable = R.drawable.ic_sixteenth_note_front_connected},
    SixteenthNoteBothConnected {override val drawable = R.drawable.ic_sixteenth_note_both_connected},
    ThirtySecondNoteBackConnected {override val drawable = R.drawable.ic_thirty_second_note_back_connected},
    ThirtySecondNoteFrontConnected {override val drawable = R.drawable.ic_thirty_second_note_front_connected},
    ThirtySecondNoteBothConnected {override val drawable = R.drawable.ic_thirty_second_note_both_connected},
    SixtyFourthNoteBackConnected {override val drawable = R.drawable.ic_sixty_fourth_note_back_connected},
    SixtyFourthNoteFrontConnected {override val drawable = R.drawable.ic_sixty_fourth_note_front_connected},
    SixtyFourthNoteBothConnected {override val drawable = R.drawable.ic_sixty_fourth_note_both_connected},
    OneHundredTwentyEighthNoteBackConnected {override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_back_connected},
    OneHundredTwentyEighthNoteFrontConnected {override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_front_connected},
    OneHundredTwentyEighthNoteBothConnected {override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_both_connected};

    abstract val drawable: Int
}

class PersistentNote(musicSettingsIndex: Int, noteIndex: Int, activity: Activity) :
    Persist(activity) {

    /* strings for sharedPref */
    private val levelString = "level $musicSettingsIndex $noteIndex"
    private val noteImageString = "note $musicSettingsIndex $noteIndex"

    /* backing fields */
    private var _level: NoteIntensity by mutableStateOf(
        get(levelString, NoteIntensity.Normal)
    )
    private var _noteImage: Int by mutableStateOf(
        get(noteImageString, NoteType.QuarterNote.drawable)
    )

    var level: NoteIntensity
        get() = _level
        set(value) {
            _level = put(value, levelString)
        }

    var noteImage: Int
        get() = _noteImage
        set(value) {
            _noteImage = value
        }

    override fun reset() {
        level = NoteIntensity.Normal
        noteImage = NoteType.QuarterNote.drawable
    }
}

/**
 * holder for settings
 */
class PersistentMusicSegment(private val activity: Activity, private val index: Int) : Persist(activity) {
    /* strings for sharedPref */
    private val numeratorString = "numerator $index"
    private val denominatorString = "denominator $index"
    private val bpmString = "bpm $index"
    private val subdivisionString = "subdivision $index"

    /* backing fields */
    private var _numerator: Int by mutableStateOf(
        get(numeratorString, 4)
    )
    private var _denominator: Int by mutableStateOf(
        get(denominatorString, 4)
    )
    private var _bpm: Int by mutableStateOf(
        get(bpmString, 100)
    )
    private var _subdivision: Int by mutableStateOf(
        get(subdivisionString, 1)
    )

    var numerator: Int
        get() = _numerator
        set(value) {
            _numerator = when {
                value < 1 -> put(1, numeratorString)
                value > 999 -> put(999, numeratorString)
                else -> put(value, numeratorString)
            }
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            _denominator = when {
                value < 1 -> put(1, denominatorString)
                value > 64 -> put(64, denominatorString)
                else -> put(value, denominatorString)
            }
        }

    var bpm: Int
        get() = _bpm
        set(value) {
            _bpm = when {
                value < 1 -> put(1, bpmString)
                value > 999 -> put(999, bpmString)
                else -> put(value, bpmString)
            }
        }

    var subdivision: Int
        get() = _subdivision
        set(value) {
            _subdivision = when {
                value < 1 -> put(1, subdivisionString)
                else -> put(value, subdivisionString)
            }
        }

    val numOfNotes: Int
        get() = subdivision * numerator

    operator fun get(i: Int): PersistentNote {
        require(i in 0 until numOfNotes)
        val note = PersistentNote(index, i, activity)
        note.noteImage = when (denominator) {
            1 -> NoteType.WholeNote.drawable
            2 -> NoteType.HalfNote.drawable
            4 -> NoteType.QuarterNote.drawable
            else -> throw Exception("NOT IMPLEMENTED")

        }
        return note
    }

    override fun reset() {
        numerator = 4
        denominator = 4
        bpm = 100
    }
}

/**
 * holder that is a list so that multiple music settings can be loaded
 */
class PersistentMusicSegmentList(private val activity: Activity) : Persist(activity) {
    /* strings for sharedPref */
    private val countString = "count"

    /* backing fields */
    private var _count: Int by mutableStateOf(
        get(countString, 1)
    )

    var count: Int
        get() = _count
        private set(value) {
            _count = when {
                value < 1 -> put(1, countString)
                else -> put(value, countString)
            }
        }

    operator fun get(i: Int): PersistentMusicSegment {
        require(i in 0 until count)
        return PersistentMusicSegment(activity, i)
    }

    fun add() {
        count++
    }

    fun remove(i: Int) {
        repeat((count - 1) - i) {
            val index = it + i
            val copyTo = PersistentMusicSegment(activity, index)
            val copyFrom = PersistentMusicSegment(activity, index + 1)
            copyTo.denominator = copyFrom.denominator
            copyTo.numerator = copyFrom.numerator
            copyTo.bpm = copyFrom.bpm
        }
        val cleanMusicSettings = PersistentMusicSegment(activity, --count)
        cleanMusicSettings.reset()
    }

    fun asList(): List<PersistentMusicSegment> {
        val tempList = mutableListOf<PersistentMusicSegment>()
        for (index in 0 until count) {
            tempList.add(PersistentMusicSegment(activity, index))
        }
        return tempList.toList()
    }

    /**
     * Doesn't allow empty list, but resets everything in the list and makes it only one element.
     */
    override fun reset() {
        while (count != 1) {
            remove(count - 1)
        }
        get(0).reset()
    }
}

/**
 * holds certain states of the app
 */
class PersistentAppSettings(activity: Activity) : Persist(activity) {
    /* strings for sharedPref */
    private val timeSignatureExpandedString = "timeSignatureExpanded"
    private val currentMusicSettingsString = "currentMusicSettings"

    /* backing fields */
    private var _timeSignatureExpanded: Boolean by mutableStateOf(
        get(timeSignatureExpandedString, false)
    )
    private var _currentMusicSettings: Int by mutableStateOf(
        get(currentMusicSettingsString, 0)
    )

    var timeSignatureExpanded: Boolean
        get() = _timeSignatureExpanded
        set(value) {
            _timeSignatureExpanded = put(value, timeSignatureExpandedString)
        }

    var currentMusicSettings: Int
        get() = _currentMusicSettings
        set(value) {
            _currentMusicSettings = when {
                value < 0 -> put(0, currentMusicSettingsString)
                else -> put(value, currentMusicSettingsString)
            }
        }

    override fun reset() {
        timeSignatureExpanded = false
        currentMusicSettings = 0
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