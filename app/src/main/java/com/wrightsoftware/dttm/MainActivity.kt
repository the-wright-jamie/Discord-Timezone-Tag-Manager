package com.wrightsoftware.dttm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.wrightsoftware.dttm.ui.theme.DTTMTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DTTMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog(
                        onDismissRequest = { println("Dismiss request") },
                        onDateChange = { println("date changed to $it") }
                    )
                }
            }
        }
    }
}