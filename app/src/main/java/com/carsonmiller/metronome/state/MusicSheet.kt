package com.carsonmiller.metronome.state

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.state.enums.NoteIntensity
import com.carsonmiller.metronome.state.enums.NoteType
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import java.util.*

/**
 * holder for settings
 */
class MusicSheet(private val index: Int) : Savable {

    /* strings for sharedPref */
    private val numeratorString = "numerator $index"
    private val denominatorString = "denominator $index"
    private val rawBpmString = "rawBPM $index"
    private val subdivisionString = "subdivision $index"
    private val currentNoteString = "currentNote $index"

    /* backing fields */
    private var _numerator: Int by mutableStateOf(
        Store.volatileGet(numeratorString, 4)
    )
    private var _denominator: Int by mutableStateOf(
        Store.volatileGet(denominatorString, 4)
    )
    private var _rawBPM: Int by mutableStateOf(
        Store.volatileGet(rawBpmString, 100)
    )
    private var _subdivision: Int by mutableStateOf(
        Store.volatileGet(subdivisionString, 1)
    )
    private var _currentNote: Int by mutableStateOf(
        Store.volatileGet(currentNoteString, 0)
    )

    var numerator: Int
        get() = _numerator
        set(value) {
            _numerator =
                Store.volatilePut(when {
                value < 1 -> 1
                value > 99 -> 99
                else -> value
            }, numeratorString)
            updateAllNoteImages()
        }

    var denominator: Int
        get() = _denominator
        set(value) {
            _denominator = Store.volatilePut(when {
                value < 1 -> 1
                value > 32 -> 32
                else -> value
            }, denominatorString)
            updateAllNoteImages()
        }

    var rawBPM: Int
        get() = _rawBPM
        set(value) {
            _rawBPM = Store.volatilePut(when {
                value < 1 -> 1
                value > 999 -> 999
                else -> value
            }, rawBpmString)
        }

    val bpm: Int
        get() = rawBPM * subdivision

    var subdivision: Int
        get() = _subdivision
        set(value) {
            _subdivision = Store.volatilePut(when {
                value < 1 -> 1
                else -> value
            }, subdivisionString)
            updateAllNoteImages()
        }

    var currentNote: Int
        get() = _currentNote
        set(value) {
            _currentNote = Store.volatilePut(when {
                value < 0 -> 0
                value > numOfNotes - 1 -> numOfNotes - 1
                else -> value
            }, currentNoteString)
        }

    val numOfNotes: Int
        get() = subdivision * numerator

    operator fun get(i: Int) = Note(index, i)

    private fun getNoteImage(index: Int, level: NoteIntensity): Int {
        fun connection(
            i: Int,
            numOfNotes: Int,
            groupSize: Int = (denominator * subdivision) / 4
        ): Int {
            fun distanceFromRest(i: Int): Int {
                for (j in i downTo 0) {
                    if (Note(this.index, j).level == NoteIntensity.Rest)
                        return i - j
                }
                return i - groupSize + 1
            }
            val restDist = distanceFromRest(i)
            return if (groupSize % 3 != 0) when {
                i == 0 -> 2
                restDist % groupSize == 1 -> 2
                restDist % groupSize == 0 -> 0
                get( i + 1).level == NoteIntensity.Rest -> 0
                i == numOfNotes - 1 -> 0
                else -> 1
            } else when {
                i == 0 -> 2
                restDist == 1 -> 2
                restDist == 0 -> 0
                Note(
                    index,
                    i + 1,
                ).level == NoteIntensity.Rest && i % 3 != 0 -> 0
                i % 3 == 0 -> 2
                i % 3 == 2 && restDist != 1 -> 0
                i == numOfNotes - 1 -> 0
                else -> 1
            }
        }
        require(index in 0 until numOfNotes)
        return if (level == NoteIntensity.Rest)
            when (denominator * subdivision) {
                1 -> NoteType.WholeRest.drawable
                2, 3 -> NoteType.HalfRest.drawable
                4, 6 -> NoteType.QuarterRest.drawable
                8, 12 -> NoteType.EighthRest.drawable
                16, 24 -> NoteType.SixteenthRest.drawable
                32, 48 -> NoteType.ThirtySecondRest.drawable
                64, 96 -> NoteType.SixtyFourthRest.drawable
                128 -> NoteType.OneHundredTwentyEighthRest.drawable
                else -> throw Exception("This denominator doesn't exist!")
            }
        else {
            val connection = connection(index, numOfNotes)
            when (denominator * subdivision) {
                1 -> NoteType.WholeNote.drawable
                2, 3 -> NoteType.HalfNote.drawable
                4, 6 -> NoteType.QuarterNote.drawable
                8, 12 -> when (connection) {
                    0 -> NoteType.EighthNoteFrontConnected.drawable
                    1 -> NoteType.EighthNoteBothConnected.drawable
                    else -> NoteType.EighthNoteBackConnected.drawable
                }
                16, 24 -> when (connection) {
                    0 -> NoteType.SixteenthNoteFrontConnected.drawable
                    1 -> NoteType.SixteenthNoteBothConnected.drawable
                    else -> NoteType.SixteenthNoteBackConnected.drawable
                }
                32, 48 -> when (connection) {
                    0 -> NoteType.ThirtySecondNoteFrontConnected.drawable
                    1 -> NoteType.ThirtySecondNoteBothConnected.drawable
                    else -> NoteType.ThirtySecondNoteBackConnected.drawable
                }
                64, 96 -> when (connection) {
                    0 -> NoteType.SixtyFourthNoteFrontConnected.drawable
                    1 -> NoteType.SixtyFourthNoteBothConnected.drawable
                    else -> NoteType.SixtyFourthNoteBackConnected.drawable
                }
                128 -> when (connection) {
                    0 -> NoteType.OneHundredTwentyEighthNoteFrontConnected.drawable
                    1 -> NoteType.OneHundredTwentyEighthNoteBothConnected.drawable
                    else -> NoteType.OneHundredTwentyEighthNoteBackConnected.drawable
                }
                else -> throw Exception("This denominator doesn't exist!")
            }
        }
    }

    private fun updateNoteImage(i: Int) {
        val note = Note(index, i)
        note.noteImage = getNoteImage(i, note.level)
        note.accentImage = when (note.level) {
            NoteIntensity.Loud -> R.drawable.ic_accent
            NoteIntensity.Quiet -> R.drawable.ic_soft
            else -> R.drawable.ic_blank
        }
    }

    fun updateAllNoteImages() {
        for (i in 0 until numOfNotes) {
            updateNoteImage(i)
        }
    }

    fun asList(): ImmutableList<Note> {
        val mutableList = mutableListOf<Note>()
        repeat(numOfNotes) {
            mutableList.add(get(it))
        }
        return ImmutableList.copyOf(mutableList)
    }

    fun reset() {
        numerator = 4
        denominator = 4
        rawBPM = 100
        subdivision = 1
        currentNote = 0
    }

    override fun save() {
        Store.put(numerator, numeratorString)
        Store.put(denominator, denominatorString)
        Store.put(rawBPM, rawBpmString)
        Store.put(subdivision, subdivisionString)

        repeat(numOfNotes) {
            get(it).save()
        }
    }

    override fun load() {
        numerator = Store.get(numeratorString, 4)
        denominator = Store.get(denominatorString, 4)
        rawBPM = Store.get(rawBpmString, 100)
        subdivision = Store.get(subdivisionString, 1)

        repeat(numOfNotes) {
            get(it).load()
        }
    }
}
