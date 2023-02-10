package com.wrightsoftware.dttm

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wrightsoftware.dttm.ui.theme.DTTMTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DTTMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(30.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val showDateDialog = remember { mutableStateOf(false) }
                    val showTimeDialog = remember { mutableStateOf(false) }

                    val dateChanged = remember { mutableStateOf(false) }
                    val timeChanged = remember { mutableStateOf(false) }

                    val date = remember {
                        mutableStateOf(LocalDate.parse("2000-01-01"))
                    }
                    val time = remember { mutableStateOf(LocalTime.parse("00:00")) }
                    val unixTime = remember {
                        mutableStateOf(0L)
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        //Text("Date:\n${if (dateChanged.value) date.value else "-"}")
                        Text(
                            "Date:\n${
                                if (dateChanged.value) "${
                                    date.value.format(
                                        DateTimeFormatter.ofLocalizedDate(
                                            FormatStyle.FULL
                                        )
                                    )
                                } (${
                                    date.value.format(
                                        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                    )
                                })" else "-"
                            }"
                        )
                        Spacer(Modifier.size(5.dp))
                        Button(onClick = {
                            showDateDialog.value = true
                        }) {
                            Icon(Icons.Rounded.CalendarMonth, null)
                            Spacer(Modifier.size(5.dp))
                            Text("Select Date")
                        }


                        if (showDateDialog.value) {
                            com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog(
                                onDismissRequest = {
                                    showDateDialog.value = false
                                },
                                onDateChange = {
                                    date.value = it
                                    dateChanged.value = true

                                    val ldt = LocalDateTime.of(date.value, time.value)
                                    val ldtZoned = ldt.atZone(ZoneId.systemDefault())
                                    val utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"))

                                    unixTime.value = utcZoned.toEpochSecond()
                                    showDateDialog.value = false
                                },
                                title = {
                                    Text("Select Date")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.size(15.dp))

                        Text("Time:\n${if (timeChanged.value) time.value else "-"}")
                        Spacer(Modifier.size(5.dp))
                        Button(onClick = {
                            showTimeDialog.value = true
                        }) {
                            Icon(Icons.Rounded.Schedule, null)
                            Spacer(Modifier.size(5.dp))
                            Text("Select Time")
                        }

                        if (showTimeDialog.value) {
                            com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog(
                                onDismissRequest = {
                                    showTimeDialog.value = false
                                },
                                onTimeChange = {
                                    time.value = it
                                    timeChanged.value = true

                                    val ldt = LocalDateTime.of(date.value, time.value)
                                    val ldtZoned = ldt.atZone(ZoneId.systemDefault())
                                    val utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"))

                                    unixTime.value = utcZoned.toEpochSecond()
                                    showTimeDialog.value = false
                                },
                                title = {
                                    Text("Select Time")
                                }
                            )
                        }

                        Spacer(Modifier.size(15.dp))

                        var expanded by remember { mutableStateOf(false) }
                        var selectedOptionText by remember { mutableStateOf("Default") }
                        var selectedOptionId by remember { mutableStateOf(">") }

                        Text("Display Mode:")
                        Spacer(Modifier.size(5.dp))
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
                            expanded = !expanded
                        }) {
                            TextField(
                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                modifier = Modifier.menuAnchor(),
                                readOnly = true,
                                value = selectedOptionText,
                                onValueChange = {},
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Default") },
                                    onClick = {
                                        selectedOptionText = "Default"
                                        selectedOptionId = ">"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Short Date") },
                                    onClick = {
                                        selectedOptionText = "Short Date"
                                        selectedOptionId = ":d>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Long Date") },
                                    onClick = {
                                        selectedOptionText = "Long Date"
                                        selectedOptionId = ":D>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Short Date and Time") },
                                    onClick = {
                                        selectedOptionText = "Short Date and Time"
                                        selectedOptionId = ":f>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Long Date and Time") },
                                    onClick = {
                                        selectedOptionText = "Long Date and Time"
                                        selectedOptionId = ":F>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Time Only") },
                                    onClick = {
                                        selectedOptionText = "Time Only"
                                        selectedOptionId = ":t>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Time Only + Seconds") },
                                    onClick = {
                                        selectedOptionText = "Time Only + Seconds"
                                        selectedOptionId = ":T>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                                DropdownMenuItem(
                                    text = { Text("Relative to Now") },
                                    onClick = {
                                        selectedOptionText = "Relative to Now"
                                        selectedOptionId = ":R>"
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }

                        Spacer(Modifier.size(60.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!dateChanged.value || !timeChanged.value) {
                                Text("Input Time:\n-", textAlign = TextAlign.Center)
                                Spacer(Modifier.size(10.dp))
                                Text("Resultant Tag:\n-", textAlign = TextAlign.Center)
                            } else {
                                val ldt = LocalDateTime.of(date.value, time.value)
                                val ldtZoned = ldt.atZone(ZoneId.systemDefault())
                                val utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"))

                                val tag = "<t:${unixTime.value}${selectedOptionId}"

                                Text(
                                    "Input Time:\n" +
                                            "Local Time: ${
                                                ldt.format(
                                                    DateTimeFormatter.ofLocalizedDateTime(
                                                        FormatStyle.MEDIUM
                                                    )
                                                )
                                            }\n" +
                                            "UTC Time: ${
                                                utcZoned.format(
                                                    DateTimeFormatter.ofLocalizedDateTime(
                                                        FormatStyle.MEDIUM
                                                    )
                                                )
                                            }", textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.size(10.dp))
                                Text("Resultant Tag:\n$tag", textAlign = TextAlign.Center)
                            }
                            Spacer(Modifier.size(15.dp))
                            Button(onClick = {
                                val clipboard: ClipboardManager =
                                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip: ClipData = ClipData.newPlainText(
                                    "tag",
                                    "<t:${unixTime.value}${selectedOptionId}"
                                )
                                clipboard.setPrimaryClip(clip)
                            }) {
                                Icon(Icons.Rounded.ContentCopy, null)
                                Spacer(Modifier.size(5.dp))
                                Text("Copy Tag")
                            }
                        }
                    }
                }
            }
        }
    }
}