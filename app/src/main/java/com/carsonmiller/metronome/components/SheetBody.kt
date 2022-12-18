package com.carsonmiller.metronome.components

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.layoutId
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.state.*
import com.carsonmiller.metronome.state.enums.NoteIntensity
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList
import kotlin.math.max

@Composable
fun SheetBody(modifier: Modifier = Modifier, height: Dp, numOfNotes: Int, subdivision: Int, musicSheetIndex: Int) =
    ConstraintContainer(
        modifier = modifier,
        height = height,
        fillMaxWidth = true,
        constraintSet = sheetBodyConstraint()
    ) {
        val musicStaff = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.ic_music_staff)

        //bar
        MusicStaff(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleX = 10f, scaleY = 1f)
                .offset(y = 25.dp)
                .layoutId("bar"),
            musicStaffImage = BitmapPainter(image = musicStaff.asImageBitmap()))

        //notes
        LazyRow(
            modifier = Modifier
                .layoutId("row")
        ) {
            item {
                Contents(
                    modifier = Modifier.height(height),
                    numOfNotes = numOfNotes,
                    subdivision = subdivision,
                    musicSheetIndex = musicSheetIndex)
            }
        }
    }

@Composable
private fun MusicStaff(modifier: Modifier = Modifier, musicStaffImage: Painter) =
    Image(
        modifier = modifier,
        painter = musicStaffImage,
        contentDescription = "Music Bar",
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))

@Composable
private fun Contents(modifier: Modifier = Modifier, numOfNotes: Int, subdivision: Int, musicSheetIndex: Int) {
    val size = 45
    ConstraintLayout(
        constraintSet = sheetConstraint(),
        modifier = modifier
            .width((size * (numOfNotes + 1)).dp)
    ) {
        val onBackground = MaterialTheme.colorScheme.onBackground
        val secondary = MaterialTheme.colorScheme.secondary
        TripletIndicators(
            Modifier
                .layoutId("tripletIndicators"),
            numOfNotes = numOfNotes,
            subdivision = subdivision,
            size = size
        )
        Notes(
            Modifier
                .layoutId("notes"),
            numOfNotes = numOfNotes,
            subdivision = subdivision,
            musicSheetIndex = musicSheetIndex,
            size = size,
            onBackground = onBackground,
            secondary = secondary
        )
    }
}

@Composable
private fun TripletIndicators(modifier: Modifier = Modifier, numOfNotes: Int, subdivision: Int,  size: Int) =
    LazyRow(
    modifier,
    userScrollEnabled = false
    ) {
        items(max(numOfNotes / 3, 1)) {
            repeat(max(numOfNotes / 3, 1)) {
                Image(
                    painterResource(id = R.drawable.ic_triplet_indicator),
                    modifier = Modifier.size(((size * 3) * 1.002).dp),
                    contentDescription = "triplet indicator",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                    alpha = if (subdivision == 3) 1f else 0f
                )
            }
        }
    }

@Composable
private fun Notes(
    modifier: Modifier = Modifier,
    numOfNotes: Int,
    subdivision: Int,
    musicSheetIndex: Int,
    size: Int,
    onBackground: Color,
    secondary: Color) {
    LazyRow(
        state = remember {LazyListState()},
        modifier = modifier,
        userScrollEnabled = false,
    ) {
        items(numOfNotes) { noteNum ->
            val color = remember {if (noteNum % subdivision == 0) onBackground else secondary}
            val note = Note(musicSheetIndex,noteNum)
            Note(
                modifier = Modifier
                    .height(size.dp)
                    .offset(y = (12).dp),
                noteImage = note.noteImage,
                accentImage = note.accentImage,
                color = color,
                updateNoteIntensity = remember {{
                    note.level = when (note.level) {
                        NoteIntensity.Rest -> NoteIntensity.Quiet
                        NoteIntensity.Quiet -> NoteIntensity.Normal
                        NoteIntensity.Normal -> NoteIntensity.Loud
                        NoteIntensity.Loud -> NoteIntensity.Rest
                    }
                }}
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
    noteImage: Int,
    accentImage: Int,
    color: Color,
    updateNoteIntensity: () -> Unit) {
    Column {
        Image(
            modifier = modifier
                .scale(1.004f) //hardcoded values for alignment
                .clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null
                ) { updateNoteIntensity() },
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