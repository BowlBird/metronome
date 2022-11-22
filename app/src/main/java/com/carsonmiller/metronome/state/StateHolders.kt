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
    OneHundredTwentyEighthNoteBothConnected {override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_both_connected},
    WholeRest {override val drawable = R.drawable.ic_whole_rest},
    HalfRest {override val drawable = R.drawable.ic_half_rest},
    QuarterRest {override val drawable = R.drawable.ic_quarter_rest},
    EighthRest {override val drawable = R.drawable.ic_eighth_rest},
    SixteenthRest {override val drawable = R.drawable.ic_sixteenth_rest},
    ThirtySecondRest {override val drawable = R.drawable.ic_thirty_second_rest},
    SixtyFourthRest {override val drawable = R.drawable.ic_sixty_fourth_rest},
    OneHundredTwentyEighthRest {override val drawable = R.drawable.ic_one_hundred_twenty_eighth_rest};

    abstract val drawable: Int
}

class PersistentNote(musicSettingsIndex: Int, noteIndex: Int, activity: Activity) :
    Persist(activity) {

    /* strings for sharedPref */
    private val levelString = "level $musicSettingsIndex $noteIndex"
    private val noteImageString = "note $musicSettingsIndex $noteIndex"

    /* backing fields */
    private var _level: NoteIntensity by mutableStateOf(
        NoteIntensity.valueOf(get(levelString, "Normal"))
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
                value > 99 -> put(99, numeratorString)
                else -> put(value, numeratorString)
            }
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            _denominator = when {
                value < 1 -> put(1, denominatorString)
                value > 32 -> put(32, denominatorString)
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
        fun connection(i: Int, groupSize: Int): Int {
            fun distanceFromRest(i: Int):Int {
                for(j in i downTo 0) {
                    if(PersistentNote(index, j, activity).level == NoteIntensity.Rest)
                        return i - j
                }
                return i - groupSize + 1
            }
            return when {
                i == 0 -> 2
                distanceFromRest(i) % groupSize == 1 -> 2
                distanceFromRest(i) % groupSize == 0 -> 0
                PersistentNote(index, i + 1, activity).level == NoteIntensity.Rest -> 0
                else -> 1
            }
        }
        require(i in 0 until numOfNotes)
        val note = PersistentNote(index, i, activity)
        note.noteImage =
            if(note.level == NoteIntensity.Rest)
                when (denominator * subdivision) {
                    1 -> NoteType.WholeRest.drawable
                    2,3 -> NoteType.HalfRest.drawable
                    4,6 -> NoteType.QuarterRest.drawable
                    8,12 -> NoteType.EighthRest.drawable
                    16,24 -> NoteType.SixteenthRest.drawable
                    32,48 -> NoteType.ThirtySecondRest.drawable
                    64,96 -> NoteType.SixtyFourthRest.drawable
                    128 -> NoteType.OneHundredTwentyEighthRest.drawable
                    else -> throw Exception("This denominator doesn't exist!")
                }
            else
                when (denominator * subdivision) {
            1 -> NoteType.WholeNote.drawable
            2,3 -> NoteType.HalfNote.drawable
            4,6 -> NoteType.QuarterNote.drawable
            8,12 -> when (connection(i, 4)) {
                0 -> NoteType.EighthNoteFrontConnected.drawable
                1 -> NoteType.EighthNoteBothConnected.drawable
                else -> NoteType.EighthNoteBackConnected.drawable
            }
            16,24 -> when (connection(i, 4)) {
                0 -> NoteType.SixteenthNoteFrontConnected.drawable
                1 -> NoteType.SixteenthNoteBothConnected.drawable
                else -> NoteType.SixteenthNoteBackConnected.drawable
            }
            32,48 -> when (connection(i, 8)) {
                0 -> NoteType.ThirtySecondNoteFrontConnected.drawable
                1 -> NoteType.ThirtySecondNoteBothConnected.drawable
                else -> NoteType.ThirtySecondNoteBackConnected.drawable
            }
            64,96 -> when (connection(i, 16)) {
                0 -> NoteType.SixtyFourthNoteFrontConnected.drawable
                1 -> NoteType.SixtyFourthNoteBothConnected.drawable
                else -> NoteType.SixtyFourthNoteBackConnected.drawable
            }
            128 -> when (connection(i, 32)) {
                0 -> NoteType.OneHundredTwentyEighthNoteFrontConnected.drawable
                1 -> NoteType.OneHundredTwentyEighthNoteBothConnected.drawable
                else -> NoteType.OneHundredTwentyEighthNoteBackConnected.drawable
            }
            else -> throw Exception("This denominator doesn't exist!")
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
        val cornerRounding: Dp = 8.dp //for rounded shapes

        /* padding */
        val containerSidePadding: Dp = 32.dp
        val containerHeightPadding: Dp = 0.dp
        val innerPadding: Dp = 10.dp //for inside containers

        /* margins */
        val containerMargins: Dp = 20.dp

        /* container heights */
        val headerContainerHeight: Dp = 140.dp
        val buttonContainerHeight: Dp = 80.dp
        val smallButtonContainerHeight: Dp = 25.dp
        val settingsContainerHeight: Dp = 400.dp
    }
}