package com.carsonmiller.metronome.state

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import kotlinx.coroutines.*

/**
 * holds certain states of the app
 */
class AppSettings : Savable{
    /* strings for sharedPref */
    private val playingString = "playing"

    /* backing fields */
    private var _playing: Boolean by mutableStateOf(
        Store.volatileGet(playingString, false)
    )

    var playing: Boolean
        get() = _playing
        set(value) {
            _playing = Store.volatilePut(value, playingString)
        }

    fun reset() {
        playing = false
    }

    override fun save() {
        Store.put(playing, playingString)
    }

    override fun load() {
        playing = Store.get(playingString, false)
    }
}
