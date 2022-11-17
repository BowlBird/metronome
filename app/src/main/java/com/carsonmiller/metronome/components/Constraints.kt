package com.carsonmiller.metronome.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import com.carsonmiller.metronome.ScreenSettings
import org.json.JSONObject


fun containerConstraints() = ConstraintSet {

    val bpmText = createRefFor("bpmText")
    val header = createRefFor("headerBox")
    val buttonBox = createRefFor("buttonBox")
    val settingsBox = createRefFor("settingsBox")

    constrain(bpmText) {
        end.linkTo(
            parent.end, margin = ScreenSettings.containerSidePadding * 2
        ) //*2 for some contrast
        bottom.linkTo(
            header.top, margin = ScreenSettings.containerMargins / 2 // / 2 to make it closer
        )
    }

    constrain(header) {
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
        top.linkTo(topText.top, margin = (fontSize / 2).dp) //math to make it sit on top no matter
        centerHorizontallyTo(parent, .5f)              //the font size
    }
}

fun motionTimeSignatureConstraint(expanded: Boolean) : ConstraintSet {
    val verticalPadding = if(expanded) ScreenSettings.innerPadding.value else -100
    val horizontalPadding = if(expanded) ScreenSettings.innerPadding.value else 0
    return ConstraintSet( JSONObject()
        .put("timeSignature", JSONObject()
            .put("centerVertically", "'parent'")
            .put("centerHorizontally", "'parent'")
        )
        .put("topLeft", JSONObject()
            .put("start", listOf("'parent'", "'start'", horizontalPadding))
            .put("top", listOf("'parent'", "'top'", verticalPadding))
        )
        .put("topRight", JSONObject()
            .put("end", listOf("'parent'", "'end'", horizontalPadding))
            .put("top", listOf("'parent'", "'top'", verticalPadding))
        )
        .put("bottomLeft", JSONObject()
            .put("start", listOf("'parent'", "'start'", horizontalPadding))
            .put("bottom", listOf("'parent'", "'bottom'", verticalPadding))
        )
        .put("bottomRight", JSONObject()
            .put("end", listOf("'parent'", "'end'", horizontalPadding))
            .put("bottom", listOf("'parent'", "'bottom'", verticalPadding))
        ).toString(2).replace("\"", ""))
}

fun motionHeaderConstraint(maxWidth: Float, expanded: Boolean): ConstraintSet {
    val height = ScreenSettings.headerContainerHeight.value
    val padding = ScreenSettings.innerPadding.value
    val maxContainerWidth = maxWidth - ScreenSettings.containerMargins.value * 2 - padding * 5
    val timeSignatureContainerWidth = if(expanded) (maxContainerWidth / 2) else 80
    return ConstraintSet( JSONObject()
        .put("timeSignatureContainer", JSONObject()
            .put("width", timeSignatureContainerWidth)
            .put("height", height - padding * 2)
            .put("top", listOf("'parent'", "'top'", padding))
            .put("bottom", listOf("'parent'", "'bottom'", padding))
            .put("start", listOf("'parent'", "'start'", padding))
            .put("end", listOf("'noteContainer'", "'start'", padding / 2))
        )
        .put("noteContainer", JSONObject()
            .put("width", maxContainerWidth - timeSignatureContainerWidth.toFloat())
            .put("height", height - padding * 2)
            .put("top", listOf("'parent'", "'top'", padding))
            .put("bottom", listOf("'parent'", "'bottom'", padding))
            .put("start", listOf("'timeSignatureContainer'", "'end'", padding / 2))
            .put("end", listOf("'parent'", "'end'", padding))
        ).toString(2).replace("\"", ""))
}