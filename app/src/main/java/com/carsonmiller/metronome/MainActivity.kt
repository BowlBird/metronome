package com.carsonmiller.metronome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.carsonmiller.metronome.ui.theme.MetronomeTheme
import com.carsonmiller.metronome.ui.theme.musicFont
import com.carsonmiller.metronome.ui.theme.typography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetronomeTheme {
                MainLayout()
            }
        }
    }
}

@Preview
@Composable
fun MainLayout() {

    //Variables for consistency
    val cornerRounding = 10.dp
    val containerMargins = 20.dp
    val containerSidePadding = 32.dp
    val containerHeightPadding = 0.dp

    //val for ScrollableStaff
    val scrollHeight = 100.dp

    //val for ButtonContainer
    val buttonHeight = 80.dp

    //val for SettingsContainer
    val settingsHeight = 400.dp

    //background
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background)
    ) {

        //refs for each composable held in constraint layout
        val (scrollBox, buttonBox, settingsBox, bpmText) = createRefs()

        //Music staff container
        ScrollableStaffContainer(
            modifier = Modifier
                .fillMaxWidth()
                .height(scrollHeight)
                .padding(containerSidePadding, containerHeightPadding)
                .constrainAs(scrollBox) {
                    bottom.linkTo(buttonBox.top, margin = containerMargins)
                }
                .clip(RoundedCornerShape(cornerRounding))
                .background(color = colorScheme.primaryContainer)
        )

        //text for bpm
        BpmText(
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(bpmText) {
                    end.linkTo(parent.end, margin = containerSidePadding * 2) //*2 for some contrast
                    bottom.linkTo(
                        scrollBox.top,
                        margin = containerMargins / 2
                    ) ///2 to make it closer
                }
        )

        //Button container
        ButtonContainer(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .padding(containerSidePadding, containerHeightPadding)
                .constrainAs(buttonBox) {
                    centerVerticallyTo(parent, .4f)
                }
                .clip(RoundedCornerShape(cornerRounding))
                .background(color = colorScheme.primaryContainer)
        )

        //settings container
        SettingsContainer(
            modifier = Modifier
                .fillMaxWidth()
                .height(settingsHeight)
                .padding(containerSidePadding, containerHeightPadding)
                .constrainAs(settingsBox) {
                    top.linkTo(buttonBox.bottom, margin = containerMargins)
                }
                .clip(RoundedCornerShape(cornerRounding))
                .background(color = colorScheme.primaryContainer)
        )
    }

}

@Composable
fun ScrollableStaffContainer(modifier: Modifier) {
    val numerator = 4
    val denominator = 4
    val padding = 10.dp

    //box to hold everything in one container
    Box(modifier = modifier) {

        //time signature container
        Box(
            modifier = Modifier
                .padding(padding)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            TimeSignature(modifier = Modifier, numerator, denominator)
        }

        //Notes and Music Bar Holder
        Box(
            modifier = Modifier
                .padding(70.dp, padding, padding, padding)
                .clip(RoundedCornerShape(10.dp))
                .background(color = colorScheme.inversePrimary)
                .fillMaxHeight()
                .fillMaxWidth()
        ) {

            //music bar (doesn't actually move)
            MusicBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(0.dp, (20).dp),
                4f
            )

            //row
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(13.dp)
            ) {

                //notes
                repeat(100) {
                    Note(
                        modifier = Modifier,
                        R.drawable.ic_one_hundred_twenty_eighth_note_both_connected
                    )
                }
            }
        }
    }
}

@Composable
fun BpmText(modifier: Modifier, bpm: Int = 100) {
    ConstraintLayout(
        modifier = modifier
    ) {

        val (num, bpmText) = createRefs()

        //number
        Text(
            text = "$bpm",
            modifier = Modifier
                .constrainAs(num) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            color = colorScheme.onBackground,
            style = typography.labelLarge
        )

        //bpm text
        Text(
            text = "bpm",
            modifier = Modifier
                .constrainAs(bpmText) {
                    start.linkTo(num.end, margin = 5.dp)
                },
            color = colorScheme.secondary,
            style = typography.labelLarge,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun Note(modifier: Modifier, note: Int) {
    Image(
        painterResource(id = note),
        modifier = modifier
            .scale(1.004f)
            .wrapContentSize(),
        contentDescription = "Note",
        colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
    )
}

@Composable
fun MusicBar(modifier: Modifier, scale: Float) {
    Image(
        painterResource(id = R.drawable.ic_music_staff),
        modifier = modifier
            .scale(scale, 1f),
        contentDescription = "Music Staff",
        colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
    )
}

@Composable
fun TimeSignature(modifier: Modifier, numerator: Int = 4, denominator: Int = 4) {
    val fontSize = 70
    val spFontSize = with(LocalDensity.current) {
        (fontSize / fontScale).sp
    }

    ConstraintLayout(
        modifier = modifier
            .wrapContentSize()
    ) {

        val (topNum, bottomNum) = createRefs()
        Text(
            text = "$numerator",
            modifier = Modifier
                .constrainAs(topNum) {
                    top.linkTo(parent.top)
                },
            fontFamily = musicFont,
            fontSize = spFontSize,
            color = colorScheme.onBackground
        )

        Text(
            text = "$denominator",
            modifier = Modifier
                .constrainAs(bottomNum) {
                    top.linkTo(topNum.top, margin = 35.dp)
                },
            fontFamily = musicFont,
            fontSize = spFontSize,
            color = colorScheme.onBackground
        )
    }
}

@Composable
fun ButtonContainer(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SettingsContainer(modifier: Modifier) {
    val numOfPages = 3

    HorizontalPager(
        modifier = modifier,
        count = numOfPages,
        state = rememberPagerState()
    ) { pager ->
        when (pager) {
            0 -> {
                Text("1")
            }
            1 -> {
                Text("2")
            }
            2 -> {
                Text("3")
            }
        }
    }
}