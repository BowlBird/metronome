package com.carsonmiller.metronome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import com.carsonmiller.metronome.components.*
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.state.*

class ComposeActivity : ComponentActivity() {
    private var musicSheetList: MusicSheetList? = null
    private var appSettings: AppSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicSheetList = MusicSheetList()
        appSettings = AppSettings()

        setContent {
            MetronomeTheme {
                MainLayout(
                    musicSheetList = musicSheetList ?: MusicSheetList(),
                    appSettings = appSettings ?: AppSettings()
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()

        /* allows for storing values */
        Store.initialize(this)

        musicSheetList?.load()
        appSettings?.load()
    }

    override fun onStop() {
        super.onStop()
        musicSheetList?.save()
        appSettings?.save()
    }

}

@Composable
fun MainLayout(musicSheetList: MusicSheetList, appSettings: AppSettings) {
    ConstraintLayout(
        remember { containerConstraints() },
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
    ) {
        /* logic for what musicSetting in the list is the current one */
        val musicSheet by remember { mutableStateOf(musicSheetList[musicSheetList.currentMusicSheet]) }

        //starts metronome
        StartEngine(musicSheet, musicSheetList, appSettings)

        //text for bpm
        BpmTextBody(
            modifier = Modifier
                .layoutId("bpmText"),
            bpm = musicSheet.rawBPM,
            height = ScreenSettings.bpmTextContainerHeight
        )

        SheetBody(
            modifier = Modifier
                .layoutId("sheet"),
            height = ScreenSettings.headerContainerHeight,
            numOfNotes = musicSheet.numOfNotes,
            subdivision = musicSheet.subdivision,
            noteList = StableList(musicSheet.toList()),
            musicSheet = musicSheet
        )

        BarBody(
            modifier = Modifier
                .layoutId("bar"),
            currentNote = musicSheet.currentNote,
            numOfNotes = musicSheet.numOfNotes,
            playing = appSettings.playing,
            bpm = musicSheet.bpm,
            height = ScreenSettings.barHeight
        )

        //Button container
        val decrementRounded = remember { { musicSheet.rawBPM = getRoundedMetronomeBPM(musicSheet.rawBPM, false) } }
        val decrement = remember {{musicSheet.rawBPM -= 1}}
        val togglePlay = remember {{appSettings.playing = !appSettings.playing}}
        val increment = remember {{musicSheet.rawBPM += 1}}
        val incrementRounded = remember {{musicSheet.rawBPM = getRoundedMetronomeBPM(musicSheet.rawBPM, true)}}

        ButtonBody(
            modifier = Modifier
                .layoutId("buttonBox"),
            decrementRounded = decrementRounded,
            decrement = decrement,
            togglePlay = togglePlay,
            increment = increment,
            incrementRounded = incrementRounded,
            height = ScreenSettings.buttonContainerHeight
        )

        //settings container
        PagerContainer(
            modifier = Modifier
                .layoutId("settingsBox"),
            fillMaxWidth = true,
            height = ScreenSettings.settingsContainerHeight,
            { SettingsPage(musicSheet = musicSheet) },
            { Text("Test2") },
            { Text("Test3") })
        }
    }

@Stable
public data class StableList<E>(val list: List<E>)