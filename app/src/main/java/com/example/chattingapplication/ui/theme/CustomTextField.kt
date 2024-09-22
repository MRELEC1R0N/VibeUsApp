package com.example.chattingapplication.ui.theme

import androidx.compose.material3.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFieldColors(
    focusedIndicatorColor: Color,
    unfocusedIndicatorColor: Color,
    errorIndicatorColor: Color,
    cursorColor: Color = Color.Black,
    errorCursorColor: Color = Color.Red
): TextFieldColors {
    return androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = focusedIndicatorColor,
        unfocusedBorderColor = unfocusedIndicatorColor,
        errorBorderColor = errorIndicatorColor,cursorColor = cursorColor,
        errorCursorColor = errorCursorColor
    )
}