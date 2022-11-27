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
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.components.*
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.state.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        //Music staff container
        HeaderBody(
            modifier = Modifier
                .containerModifier(ScreenSettings.headerContainerHeight)
                .layoutId("headerBox"),
            numerator = musicSettings.numerator,
            denominator = musicSettings.denominator,
            appSettings = appSettings,
            musicSettings = musicSettings
        )

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
            val delay = ((60000 / musicSettings.bpm) / when (musicSettings.subdivision) {
                1 -> 1
                2 -> 2
                3 -> 2
                4 -> 4
                else -> throw Exception("This subdivision doesn't exist!")
            }).toLong()
            while(true) {
                delay(delay)
                if (appSettings.playing) {
                    if (musicSettings.currentNote == musicSettings.numOfNotes - 1) {
                        musicSettings.currentNote = 0

                        if (musicSettingsList.count - 1 == appSettings.currentMusicSettings)
                            appSettings.currentMusicSettings = 0
                    } else
                        musicSettings.currentNote += 1
                }
                else
                    musicSettings.currentNote = 0
            }
        }


    }