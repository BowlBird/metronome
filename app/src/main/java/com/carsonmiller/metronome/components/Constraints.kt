package com.carsonmiller.metronome.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import com.carsonmiller.metronome.ScreenSettings


fun containerConstraints() = ConstraintSet {

    val bpmText = createRefFor("bpmText")
    val scrollBox = createRefFor("scrollBox")
    val buttonBox = createRefFor("buttonBox")
    val settingsBox = createRefFor("settingsBox")

    constrain(bpmText) {
        end.linkTo(
            parent.end, margin = ScreenSettings().containerSidePadding * 2
        ) //*2 for some contrast
        bottom.linkTo(
            scrollBox.top, margin = ScreenSettings().containerMargins / 2 // / 2 to make it closer
        )
    }

    constrain(scrollBox) {
        bottom.linkTo(buttonBox.top, margin = ScreenSettings().containerMargins)
    }

    constrain(buttonBox) {
        centerVerticallyTo(parent, .35f)
        centerHorizontallyTo(parent, .5f)
    }

    constrain(settingsBox) {
        top.linkTo(buttonBox.bottom, margin = ScreenSettings().containerMargins)
    }

}


fun textConstraints() = ConstraintSet {
    val num = createRefFor("num")
    val bpmText = createRefFor("bpmText")

    constrain(num) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
    }

    constrain(bpmText) {
        start.linkTo(num.end, margin = 5.dp)
    }
}

fun bpmConstraints() = ConstraintSet {
    val topText = createRefFor("topText")
    val bottomText = createRefFor("bottomText")

    constrain(topText) {
        top.linkTo(parent.top)
    }

    constrain(bottomText) {
        top.linkTo(topText.top, margin = 35.dp)
    }
}
