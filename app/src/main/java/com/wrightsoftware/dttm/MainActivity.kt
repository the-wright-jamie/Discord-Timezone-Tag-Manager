package com.wrightsoftware.dttm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.wrightsoftware.dttm.ui.theme.DTTMTheme
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DTTMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var date = LocalDate.parse("2000-01-01")
                    var time = LocalTime.parse("00:00")
                    var unixTime = 0L

                    com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog(
                        onDismissRequest = { println("Dismiss request") },
                        onDateChange = {
                            println("date changed to $it")
                            date = it
                        },
                        title = {
                            Text("Select Date")
                        }
                    )

                    com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog(
                        onDismissRequest = { println("Dismiss request") },
                        onTimeChange = {
                            println("time changed to $it")
                            time = it
                        },
                        title = {
                            Text("Select Time")
                        }
                    )
                }
            }
        }
    }
}