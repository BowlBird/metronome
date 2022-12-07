package com.carsonmiller.metronome

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.components.*
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.state.*
import kotlinx.coroutines.delay
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetronomeTheme {
                MainLayout(
                    musicSettingsList = PersistentMusicSegmentList(this),
                    appSettings = PersistentAppSettings(this)
                )
            }
        }
    }
}

@Composable
fun MainLayout(musicSettingsList: PersistentMusicSegmentList, appSettings: PersistentAppSettings) =
    ConstraintLayout(
        remember {containerConstraints()},
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
    ) {
        /* logic for what musicSetting in the list is the current one */
        val musicSettings by remember {mutableStateOf(musicSettingsList[appSettings.currentMusicSettings])}
        //text for bpm
        BpmTextBody(
            modifier = Modifier
                .wrapContentSize()
                .layoutId("bpmText"), bpm = musicSettings.bpm
        )
        Sheet(
            modifier = Modifier
                .containerModifier(ScreenSettings.headerContainerHeight)
                .layoutId("sheet"),
            musicSettings = musicSettings,
        )
        Bar(
            modifier = Modifier
                .containerModifier(5.dp)
                .layoutId("bar"),
            musicSettings.currentNote, musicSettings.numOfNotes)

        //Button container
        ButtonBody(
            modifier = Modifier
                .containerModifier(ScreenSettings.buttonContainerHeight)
                .layoutId("buttonBox"),
            settings = musicSettings, //only pass in settings when state is being changed.
            appSettings = appSettings
        )

        //settings container
        PagerContainer(modifier = Modifier
            .containerModifier(ScreenSettings.settingsContainerHeight)
            .layoutId("settingsBox"),
            { SettingsPage(musicSegmentState = musicSettings)},
            { Text("Test2") },
            { Text("Test3") })


            //increments current note if the metronome is playing
            LaunchedEffect(musicSettings.bpm + musicSettings.subdivision) { //calculation is meaningless, just to make it reset when either value changes
                val delay = (60000f / musicSettings.subdivBPM).toLong()
                while (true) {
                    delay(delay)
                    if (appSettings.playing) {
                        if (musicSettings.currentNote == musicSettings.numOfNotes - 1) {
                            musicSettings.currentNote = 0

                            if (musicSettingsList.count - 1 == appSettings.currentMusicSettings)
                                appSettings.currentMusicSettings = 0
                        } else
                            musicSettings.currentNote += 1
                    } else
                        musicSettings.currentNote = 0
                }
            }
        }
