package com.carsonmiller.metronome.state

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.state.enums.NoteIntensity
import com.carsonmiller.metronome.state.enums.NoteType

class Note(musicSettingsIndex: Int, noteIndex: Int, activity: Activity) : Model(activity) {

    /* strings for saving */
    private val levelString = "level $musicSettingsIndex $noteIndex"
    private val noteImageString = "note $musicSettingsIndex $noteIndex"
    private val accentImageString = "accent $musicSettingsIndex $noteIndex"

    /* backing fields */
    private var _level: NoteIntensity by mutableStateOf(
        NoteIntensity.valueOf(storage.volatileGet(levelString, "Normal"))
    )
    private var _noteImage: Int by mutableStateOf(
        storage.volatileGet(noteImageString, NoteType.QuarterNote.drawable)
    )
    private var _accentImage: Int by mutableStateOf(
        storage.volatileGet(accentImageString, R.drawable.ic_blank)
    )

    var level: NoteIntensity
        get() = _level
        set(value) {
            _level = storage.volatilePut(value, levelString)
        }

    var noteImage: Int
        get() = _noteImage
        set(value) {
            _noteImage = storage.volatilePut(value, noteImageString)
        }

    var accentImage: Int
        get() = _accentImage
        set(value) {
            _accentImage = storage.volatilePut(value, accentImageString)
        }

    fun reset() {
        level = NoteIntensity.Normal
        noteImage = NoteType.QuarterNote.drawable
    }

    override fun save() {
        storage.put(level, levelString)
        storage.put(noteImage, noteImageString)
        storage.put(accentImage, accentImageString)
    }

    override fun load() {
        level = NoteIntensity.valueOf(storage.get(levelString, "Normal"))
        noteImage = storage.get(noteImageString, NoteType.QuarterNote.drawable)
        accentImage = storage.get(accentImageString, R.drawable.ic_blank)
    }
}
