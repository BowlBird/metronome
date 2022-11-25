package com.carsonmiller.metronome

import android.os.Bundle
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
            settings = musicSettings //only pass in settings when state is being changed.
        )

        //settings container
        PagerContainer(modifier = Modifier
            .containerModifier(ScreenSettings.settingsContainerHeight)
            .layoutId("settingsBox"), { Text("Test")}, { Text("Test2") }, { Text("Test3") })
    }