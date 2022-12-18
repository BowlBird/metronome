package com.carsonmiller.metronome.state.enums

import androidx.compose.runtime.Stable
import com.carsonmiller.metronome.R

/**
 * Container enum for each note image
 */
@Stable
enum class NoteType {
    WholeNote {
        override val drawable = R.drawable.ic_whole_note
    },
    HalfNote {
        override val drawable = R.drawable.ic_half_note
    },
    QuarterNote {
        override val drawable = R.drawable.ic_quarter_note
    },
    EighthNoteBackConnected {
        override val drawable = R.drawable.ic_eighth_note_back_connected
    },
    EighthNoteFrontConnected {
        override val drawable = R.drawable.ic_eighth_note_front_connected
    },
    EighthNoteBothConnected {
        override val drawable = R.drawable.ic_eighth_note_both_connected
    },
    SixteenthNoteBackConnected {
        override val drawable = R.drawable.ic_sixteenth_note_back_connected
    },
    SixteenthNoteFrontConnected {
        override val drawable = R.drawable.ic_sixteenth_note_front_connected
    },
    SixteenthNoteBothConnected {
        override val drawable = R.drawable.ic_sixteenth_note_both_connected
    },
    ThirtySecondNoteBackConnected {
        override val drawable = R.drawable.ic_thirty_second_note_back_connected
    },
    ThirtySecondNoteFrontConnected {
        override val drawable = R.drawable.ic_thirty_second_note_front_connected
    },
    ThirtySecondNoteBothConnected {
        override val drawable = R.drawable.ic_thirty_second_note_both_connected
    },
    SixtyFourthNoteBackConnected {
        override val drawable = R.drawable.ic_sixty_fourth_note_back_connected
    },
    SixtyFourthNoteFrontConnected {
        override val drawable = R.drawable.ic_sixty_fourth_note_front_connected
    },
    SixtyFourthNoteBothConnected {
        override val drawable = R.drawable.ic_sixty_fourth_note_both_connected
    },
    OneHundredTwentyEighthNoteBackConnected {
        override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_back_connected
    },
    OneHundredTwentyEighthNoteFrontConnected {
        override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_front_connected
    },
    OneHundredTwentyEighthNoteBothConnected {
        override val drawable = R.drawable.ic_one_hundred_twenty_eighth_note_both_connected
    },
    WholeRest {
        override val drawable = R.drawable.ic_whole_rest
    },
    HalfRest {
        override val drawable = R.drawable.ic_half_rest
    },
    QuarterRest {
        override val drawable = R.drawable.ic_quarter_rest
    },
    EighthRest {
        override val drawable = R.drawable.ic_eighth_rest
    },
    SixteenthRest {
        override val drawable = R.drawable.ic_sixteenth_rest
    },
    ThirtySecondRest {
        override val drawable = R.drawable.ic_thirty_second_rest
    },
    SixtyFourthRest {
        override val drawable = R.drawable.ic_sixty_fourth_rest
    },
    OneHundredTwentyEighthRest {
        override val drawable = R.drawable.ic_one_hundred_twenty_eighth_rest
    };

    abstract val drawable: Int
}
