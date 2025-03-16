package com.example.currencyratetracking.ui_theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


val familyInter = FontFamily(
    Font(R.font.inter_24pt_bold, FontWeight.Bold),
    Font(R.font.inter_24pt_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.inter_24pt_medium, FontWeight.Medium),
    Font(R.font.inter_24pt_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.inter_24pt_regular, FontWeight.Normal),
    Font(R.font.inter_24pt_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_24pt_semibold, FontWeight.SemiBold),
    Font(R.font.inter_24pt_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
)


val familyRoboto = FontFamily(
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_semibold, FontWeight.SemiBold),
    Font(R.font.roboto_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
)


// Set of Material typography styles to start with
val Typography = Typography(

    displayLarge = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = TextDefault,
        lineHeight = 28.sp,
        textAlign = TextAlign.Start,
    ),

    //filter text: maybe to titleMedium
    titleMedium = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = TextDefault,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start,
    ),

    //card number: maybe to bodyLarge
    bodyLarge = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = TextDefault,
        lineHeight = 24.sp,
        textAlign = TextAlign.Start,
    ),

    //card text: maybe to bodyMedium
    bodyMedium = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = TextDefault,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start,
    ),

    labelLarge = TextStyle(
        fontFamily = familyRoboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        letterSpacing = 0.1.sp,
    ),

    labelMedium = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = TextSecondary,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start,
    ),

    labelSmall = TextStyle(
        fontFamily = familyInter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        textAlign = TextAlign.Center,
    ),
)


val familyDancingScript = FontFamily(
    Font(R.font.dancingscript_bold, FontWeight.Bold),
    Font(R.font.dancingscript_medium, FontWeight.Medium),
    Font(R.font.dancingscript_regular, FontWeight.Normal),
    Font(R.font.dancingscript_semibold, FontWeight.SemiBold),
)