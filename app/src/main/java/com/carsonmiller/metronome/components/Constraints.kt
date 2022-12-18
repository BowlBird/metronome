package com.carsonmiller.metronome.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import com.carsonmiller.metronome.state.ScreenSettings


fun containerConstraints() = ConstraintSet {

    val bpmText = createRefFor("bpmText")
    val sheet = createRefFor("sheet")
    val bar = createRefFor("bar")
    val buttonBox = createRefFor("buttonBox")
    val settingsBox = createRefFor("settingsBox")

    constrain(bpmText) {
        end.linkTo(
            parent.end, margin = ScreenSettings.containerSidePadding
        )
        bottom.linkTo(
            sheet.top, margin = ScreenSettings.containerMargins
        )
    }

    constrain(sheet) {
        bottom.linkTo(bar.top, margin = ScreenSettings.containerMargins)
        centerHorizontallyTo(parent)
    }

    constrain(bar) {
        bottom.linkTo(buttonBox.top, margin = ScreenSettings.containerMargins)
        centerHorizontallyTo(parent)
    }

    constrain(buttonBox) {
        centerVerticallyTo(parent, .35f)
        centerHorizontallyTo(parent, .5f)
    }

    constrain(settingsBox) {
        top.linkTo(buttonBox.bottom, margin = ScreenSettings.containerMargins)
        centerHorizontallyTo(parent)
    }
}


fun textConstraints() = ConstraintSet {
    val num = createRefFor("num")
    val bpmText = createRefFor("bpmText")

    constrain(num) {
        centerVerticallyTo(parent)
        start.linkTo(parent.start)
        top.linkTo(parent.top)
    }

    constrain(bpmText) {
        centerVerticallyTo(parent)
        start.linkTo(num.end, margin = 5.dp)
    }
}

fun settingsPageConstraint() = ConstraintSet {
    val subdivisionSlider = createRefFor("subdivisionSlider")
    val tapBPMButton = createRefFor("tapBPMButton")
    val timeSignature = createRefFor("timeSignature")

    constrain(subdivisionSlider) {
        top.linkTo(parent.top)
    }
    constrain(tapBPMButton) {
        bottom.linkTo(parent.bottom)
    }
    constrain(timeSignature) {
        top.linkTo(subdivisionSlider.bottom)
        centerHorizontallyTo(parent)
    }
}

fun sheetConstraint() = ConstraintSet {
    val notes = createRefFor("notes")
    val tripletIndicators = createRefFor("tripletIndicators")

    constrain(notes) {
        centerVerticallyTo(parent, 1f)
        bottom.linkTo(parent.bottom)
    }

    constrain(tripletIndicators) {
        top.linkTo(parent.top)
        bottom.linkTo(notes.top)
    }
}

fun sheetBodyConstraint() = ConstraintSet {
    val bar = createRefFor("bar")
    val row = createRefFor("row")

    constrain(bar) {

    }
    constrain(row) {
        centerVerticallyTo(parent)
    }
}

fun timeSignatureControlConstraint() = ConstraintSet {
    val topLeftButton = createRefFor("topLeftButton")
    val topRightButton = createRefFor("topRightButton")
    val bottomLeftButton = createRefFor("bottomLeftButton")
    val bottomRightButton = createRefFor("bottomRightButton")
    val timeSignature = createRefFor("timeSignature")

    constrain(timeSignature) {
        centerHorizontallyTo(parent)
        centerVerticallyTo(parent)
    }
    constrain(topLeftButton) {
        end.linkTo(timeSignature.start, ScreenSettings.innerPadding)
        top.linkTo(parent.top, ScreenSettings.innerPadding * 2)
    }
    constrain(topRightButton) {
        start.linkTo(timeSignature.end, ScreenSettings.innerPadding)
        top.linkTo(parent.top, ScreenSettings.innerPadding * 2)
    }
    constrain(bottomLeftButton) {
        end.linkTo(timeSignature.start, ScreenSettings.innerPadding)
        bottom.linkTo(parent.bottom, ScreenSettings.innerPadding * 2)
    }
    constrain(bottomRightButton) {
        start.linkTo(timeSignature.end, ScreenSettings.innerPadding)
        bottom.linkTo(parent.bottom, ScreenSettings.innerPadding * 2)
    }
}

fun timeSignatureConstraint(fontSize: Int) = ConstraintSet {
    val numerator = createRefFor("numerator")
    val denominator = createRefFor("denominator")

    constrain(numerator) {
        top.linkTo(parent.top, ScreenSettings.innerPadding)
        centerHorizontallyTo(parent, .5f)
    }

    constrain(denominator) {
        top.linkTo(numerator.top, margin = (fontSize / 2).dp) //math to make it sit on top no matter
        centerHorizontallyTo(parent, .5f)                //the font size
    }
}