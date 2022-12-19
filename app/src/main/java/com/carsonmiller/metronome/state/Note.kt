package com.carsonmiller.metronome.state

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.state.enums.NoteIntensity
import com.carsonmiller.metronome.state.enums.NoteType

data class Note(val musicSettingsIndex: Int, val noteIndex: Int) : Savable {

    /* strings for saving */
    private val levelString = "level $musicSettingsIndex $noteIndex"
    private val noteImageString = "note $musicSettingsIndex $noteIndex"
    private val accentImageString = "accent $musicSettingsIndex $noteIndex"

    /* backing fields */
    private var _level: NoteIntensity by mutableStateOf(
        NoteIntensity.valueOf(Store.volatileGet(levelString, "Normal"))
    )
    private var _noteImage: Int by mutableStateOf(
        Store.volatileGet(noteImageString, NoteType.QuarterNote.drawable)
    )
    private var _accentImage: Int by mutableStateOf(
        Store.volatileGet(accentImageString, R.drawable.ic_blank)
    )

    var level: NoteIntensity
        get() = _level
        set(value) {
            _level = Store.volatilePut(value, levelString)
        }

    var noteImage: Int
        get() = _noteImage
        set(value) {
            _noteImage = Store.volatilePut(value, noteImageString)
        }

    var accentImage: Int
        get() = _accentImage
        set(value) {
            _accentImage = Store.volatilePut(value, accentImageString)
        }

    fun reset() {
        level = NoteIntensity.Normal
        noteImage = NoteType.QuarterNote.drawable
    }

    override fun save() {
        Store.put(level, levelString)
        Store.put(noteImage, noteImageString)
        Store.put(accentImage, accentImageString)
    }

    override fun load() {
        level = NoteIntensity.valueOf(Store.get(levelString, "Normal"))
        noteImage = Store.get(noteImageString, NoteType.QuarterNote.drawable)
        accentImage = Store.get(accentImageString, R.drawable.ic_blank)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (musicSettingsIndex != other.musicSettingsIndex) return false
        if (noteIndex != other.noteIndex) return false
        if (level != other.level) return false
        if (noteImage != other.noteImage) return false
        if (accentImage != other.accentImage) return false

        return true
    }

    override fun hashCode(): Int {
        var result = musicSettingsIndex
        result = 31 * result + noteIndex
        result = 31 * result + level.hashCode()
        result = 31 * result + noteImage
        result = 31 * result + accentImage
        return result
    }
}
