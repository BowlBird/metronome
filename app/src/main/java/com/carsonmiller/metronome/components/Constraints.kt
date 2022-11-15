package com.carsonmiller.metronome.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import com.carsonmiller.metronome.ScreenSettings


fun containerConstraints() = ConstraintSet {

    val bpmText = createRefFor("bpmText")
    val scrollBox = createRefFor("scrollBox")
    val buttonBox = createRefFor("buttonBox")
    val settingsBox = createRefFor("settingsBox")
    val timeSignaturePopup = createRefFor("timeSignaturePopup")

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

    constrain(timeSignaturePopup) {
        centerVerticallyTo(parent, .5f)
        centerHorizontallyTo(parent, .5f)
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

fun timeSignatureConstraint(fontSize: Int) = ConstraintSet {
    val topText = createRefFor("topText")
    val bottomText = createRefFor("bottomText")

    constrain(topText) {
        top.linkTo(parent.top)
        centerHorizontallyTo(parent, .5f)
    }

    constrain(bottomText) {
        top.linkTo(topText.top, margin = (fontSize / 2).dp)
        centerHorizontallyTo(parent, .5f)
    }
}

fun timeSignatureContainerConstraint() = ConstraintSet {
    val topLeft = createRefFor("topLeft")
    val topRight = createRefFor("topRight")
    val bottomLeft = createRefFor("bottomLeft")
    val bottomRight = createRefFor("bottomRight")
    val timeSignature = createRefFor("timeSignature")

    val padding = 10.dp

    constrain(timeSignature) {
        centerVerticallyTo(parent,.5f)
        centerHorizontallyTo(parent,.5f)
    }

    constrain(topLeft) {
        end.linkTo(timeSignature.start)
        start.linkTo(parent.start)
        top.linkTo(parent.top, padding)
    }
    constrain(topRight) {
        start.linkTo(timeSignature.end)
        end.linkTo(parent.end)
        top.linkTo(parent.top, padding)
    }
    constrain(bottomLeft) {
        end.linkTo(timeSignature.start)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom, padding)
    }
    constrain(bottomRight) {
        start.linkTo(timeSignature.end)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom, padding)
    }
}
