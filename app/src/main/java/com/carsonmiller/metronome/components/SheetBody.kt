package com.carsonmiller.metronome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.state.*
import com.carsonmiller.metronome.state.enums.NoteIntensity
import kotlin.math.max

@Composable
fun SheetBody(modifier: Modifier = Modifier, musicSheet: MusicSheet) =
    ConstraintContainer(
        modifier = modifier,
        height = ScreenSettings.headerContainerHeight,
        fillMaxWidth = true,
        constraintSet = remember {sheetBodyConstraint()}
    ) {
        //bar
        Image(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = 5f, scaleY = 1f)
                .layoutId("bar"),
            painter = painterResource(id = R.drawable.ic_music_staff),
            contentDescription = "Music Bar",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
        LazyRow(
            modifier = Modifier
        ) {
            item {
                Contents(modifier = Modifier.height(125.dp), musicSettings = musicSheet)
            }
        }
    }

@Composable
fun Sheet(modifier: Modifier = Modifier, musicSettings: MusicSheet) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .height(100.dp)
                .scale(scaleX = 1000f, scaleY = 1f)
                .offset(y = (36).dp),
            painter = painterResource(id = R.drawable.ic_music_staff),
            contentDescription = "Music Bar",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        LazyRow(
            modifier = Modifier
        ) {
            item {
                Contents(modifier = Modifier.height(125.dp), musicSettings = musicSettings)
            }
        }
    }
}



@Composable
private fun Contents(modifier: Modifier = Modifier, musicSettings: MusicSheet) {
    val size = 45
    ConstraintLayout(
        constraintSet = remember { sheetConstraint() },
        modifier = modifier
            .width((size * (musicSettings.numOfNotes + 1)).dp)
    ) {
        TripletIndicators(
            Modifier
                .layoutId("tripletIndicators"),
            musicSettings = musicSettings,
            size = size
        )
        Notes(
            Modifier
                .layoutId("notes"),
            musicSettings = musicSettings,
            size = size
        )
    }
}

@Composable
private fun TripletIndicators(modifier: Modifier = Modifier, musicSettings: MusicSheet, size: Int) = LazyRow(
    modifier,
    userScrollEnabled = false) {
    items(max(musicSettings.numOfNotes / 3,1)) {
        Image(
            painterResource(id = R.drawable.ic_triplet_indicator),
            modifier = Modifier.size(((size * 3) * 1.002).dp),
            contentDescription = "triplet indicator",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
            alpha = if (musicSettings.subdivision == 3) 1f else 0f
        )
    }

}

@Composable
private fun Notes(modifier: Modifier = Modifier, musicSettings: MusicSheet, size: Int) {
    LazyRow(
        modifier,
        userScrollEnabled = false,
    ) {
        items(musicSettings.numOfNotes) { noteNum ->
            val note = musicSettings[noteNum]
            Note(
                modifier = Modifier
                    .height(size.dp)
                    .offset(y = (12).dp),
                noteLevel = note.level,
                noteImage = note.noteImage,
                accentImage = note.accentImage,
                note = note,
                musicSettings = musicSettings,
                index = noteNum,
                color = if (noteNum % musicSettings.subdivision == 0) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

/**
 * Represents and holds note value
 */
@Composable
private fun Note(
    modifier: Modifier = Modifier,
    noteLevel: NoteIntensity,
    noteImage: Int,
    accentImage: Int,
    note: Note,
    musicSettings: MusicSheet,
    index: Int,
    color: Color) {
    Column {
        Image(
            modifier = modifier
                .scale(1.004f) //hardcoded values for alignment
                .clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null
                ) {
                    note.level = when (noteLevel) {
                        NoteIntensity.Rest -> NoteIntensity.Quiet
                        NoteIntensity.Quiet -> NoteIntensity.Normal
                        NoteIntensity.Normal -> NoteIntensity.Loud
                        NoteIntensity.Loud -> NoteIntensity.Rest
                    }

                    //this sucks but it successfully redraws everything so :3
                    val denominator = musicSettings.denominator
                    musicSettings.denominator = 3
                    musicSettings.denominator = denominator
                },
            painter = painterResource(id = noteImage),
            contentDescription = "Note",
            colorFilter = ColorFilter.tint(color = color),
        )
        Image(
            painterResource(accentImage),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = (-16).dp),
            contentDescription = "Accent",
            colorFilter = ColorFilter.tint(color = color),
        )
    }
}