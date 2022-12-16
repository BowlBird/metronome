package com.carsonmiller.metronome.state

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Starts the internal engine for beats
 */
@Composable
fun StartEngine(musicSheet: MusicSheet, musicSheetList: MusicSheetList, appSettings: AppSettings) {

    //increments current note if the metronome is playing
    LaunchedEffect(musicSheet.bpm + musicSheet.subdivision + appSettings.playing.intValue) { //calculation is meaningless, just to make it reset when either value changes

        //big loop to make this run for entire playing time
        while(appSettings.playing) {

            //gets the interval between beats
            val delay = (60000f / musicSheet.bpm).toLong()

            //finally delay last so that all logic feels snappy.
            delay(if(appSettings.playing) delay else 0)

            //for the most part, just increment current note
            if (musicSheet.currentNote < musicSheet.numOfNotes - 1)
                musicSheet.currentNote += 1
            //otherwise, reset current note and increment currentMusicSheet
            else
            {
                musicSheet.currentNote = 0

                //if on last current music sheet, reset.
                if (musicSheetList.count - 1 == musicSheetList.currentMusicSheet)
                    musicSheetList.currentMusicSheet = 0
                else
                    musicSheetList.currentMusicSheet += 1
            }
        }
        //if on last current note, reset.
        musicSheet.currentNote = 0
    }
}


/**
 * used to help make the seed key value
 */
private val Boolean.intValue
    get() = if (this) 1 else 0