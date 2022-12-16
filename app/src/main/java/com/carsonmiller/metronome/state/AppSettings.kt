package com.carsonmiller.metronome.state

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import kotlinx.coroutines.*

/**
 * holds certain states of the app
 */
class AppSettings(activity: Activity) : Model(activity){
    /* strings for sharedPref */
    private val playingString = "playing"

    /* backing fields */
    private var _playing: Boolean by mutableStateOf(
        storage.volatileGet(playingString, false)
    )

    var playing: Boolean
        get() = _playing
        set(value) {
            _playing = storage.volatilePut(value, playingString)
        }

    fun reset() {
        playing = false
    }

    override fun save() {
        storage.put(playing, playingString)
    }

    override fun load() {
        playing = storage.get(playingString, false)
    }
}
