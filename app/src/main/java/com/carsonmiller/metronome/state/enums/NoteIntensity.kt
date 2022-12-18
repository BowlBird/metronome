package com.carsonmiller.metronome.state.enums

import androidx.compose.runtime.Stable

/**
 * Intensities that each note can take on
 */
@Stable
enum class NoteIntensity {
    Rest, Quiet, Normal, Loud
}
