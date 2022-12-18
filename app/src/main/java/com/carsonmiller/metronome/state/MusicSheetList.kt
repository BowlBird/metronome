package com.carsonmiller.metronome.state

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import kotlinx.coroutines.*

/**
 * holder that is a list so that multiple music settings can be loaded
 */
class MusicSheetList : Savable {

    /* strings for sharedPref */
    private val countString = "count"
    private val currentMusicSheetString = "currentMusicSheet"

    /* backing fields */
    private var _count: Int by mutableStateOf(
        Store.volatileGet(countString, 1)
    )

    private var _currentMusicSheet: Int by mutableStateOf(
        Store.volatileGet(currentMusicSheetString, 0)
    )

    var count: Int
        get() = _count
        private set(value) {
            _count = Store.volatilePut(when {
                value < 1 -> 1
                else -> value
            }, countString)
        }

    var currentMusicSheet: Int
        get() = _currentMusicSheet
        set(value) {
            _currentMusicSheet = Store.volatilePut(when {
                value < 0 -> 0
                else -> value
            }, currentMusicSheetString)
        }

    operator fun get(i: Int): MusicSheet {
        require(i in 0 until count)
        return MusicSheet(i)
    }

    fun add() {
        count++
    }

    fun remove(i: Int) {
        repeat((count - 1) - i) {
            val index = it + i
            val copyTo = MusicSheet(index)
            val copyFrom = MusicSheet(index + 1)
            copyTo.denominator = copyFrom.denominator
            copyTo.numerator = copyFrom.numerator
            copyTo.rawBPM = copyFrom.rawBPM
        }
        val cleanMusicSettings = MusicSheet(--count)
        cleanMusicSettings.reset()
    }

    /**
     * Doesn't allow empty list, but resets everything in the list and makes it only one element.
     */
    fun reset() {
        while (count != 1) {
            remove(count - 1)
        }
        get(0).reset()
    }

    override fun save() {
        Store.put(count, countString)
        Store.put(currentMusicSheet, currentMusicSheetString)

        repeat(count) {
            get(it).save()
        }
    }

    override fun load() {
        count = Store.get(countString, 1)
        currentMusicSheet = Store.get(currentMusicSheetString, 0)

        repeat(count) {
            get(it).load()
        }
    }
}
