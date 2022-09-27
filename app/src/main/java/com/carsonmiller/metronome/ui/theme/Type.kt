package com.carsonmiller.metronome.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.carsonmiller.metronome.R

private val arimoFamily = FontFamily(
    fonts = listOf(
        Font(R.font.arimo_regular),
        Font(R.font.arimo_bold, weight = FontWeight.Bold),
        Font(R.font.arimo_italic, style = FontStyle.Italic),
        Font(R.font.arimo_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.arimo_medium, weight = FontWeight.Medium),
        Font(R.font.arimo_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(R.font.arimo_semibold, weight = FontWeight.SemiBold),
        Font(R.font.arimo_semibolditalic, weight = FontWeight.SemiBold, style = FontStyle.Italic)
    )
)

val musicFont = FontFamily(

    Font(R.font.time_signature_font)
)

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = arimoFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = arimoFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = arimoFamily),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = arimoFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = arimoFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = arimoFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = arimoFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = arimoFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = arimoFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = arimoFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = arimoFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = arimoFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = arimoFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = arimoFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = arimoFamily)
)