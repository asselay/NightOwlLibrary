package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.model.formatHour
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.HourStepper
import com.nightowl.library.ui.components.QuietBadge
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.ScreenPreview
import com.nightowl.library.viewmodel.ReserveUiState
import com.nightowl.library.viewmodel.ReserveViewModel

@Composable
fun ReserveSeatScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ReserveViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }

    ReserveSeatContent(
        seat = viewModel.seat,
        zone = viewModel.zone,
        nightMode = viewModel.nightMode,
        windowStart = viewModel.windowStart,
        windowEnd = viewModel.windowEnd,
        state = state,
        onNameChange = viewModel::onNameChange,
        onStartChange = viewModel::changeStart,
        onEndChange = viewModel::changeEnd,
        onSubmit = viewModel::submit,
        onBack = onBack
    )
}

@Composable
private fun ReserveSeatContent(
    seat: Seat?,
    zone: Zone?,
    nightMode: Boolean,
    windowStart: Int,
    windowEnd: Int,
    state: ReserveUiState,
    onNameChange: (String) -> Unit,
    onStartChange: (Int) -> Unit,
    onEndChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Reserve seat", onBack = onBack)

        if (seat == null || zone == null) {
            EmptyState(message = "Seat not found.")
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${seat.name}  •  ${zone.name}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    QuietBadge(level = zone.quietLevel)
                }

                if (nightMode) {
                    Text(
                        text = "Night mode: choose a time between ${formatHour(windowStart)} and ${formatHour(windowEnd)}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedTextField(
                    value = state.studentName,
                    onValueChange = onNameChange,
                    label = { Text("Your name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                HourStepper(
                    label = "From",
                    value = formatHour(state.startHour),
                    onDecrease = { onStartChange(-1) },
                    onIncrease = { onStartChange(1) },
                    canDecrease = state.startHour > windowStart,
                    canIncrease = state.startHour < windowEnd - 1
                )
                HourStepper(
                    label = "To",
                    value = formatHour(state.endHour),
                    onDecrease = { onEndChange(-1) },
                    onIncrease = { onEndChange(1) },
                    canDecrease = state.endHour > state.startHour + 1,
                    canIncrease = state.endHour < windowEnd
                )

                state.error?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                    Text("Confirm reservation")
                }
            }
        }
    }
}

@ScreenPreview
@Composable
private fun ReserveSeatContentPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReserveSeatContent(
                seat = LibraryRepository.seats.first(),
                zone = LibraryRepository.zones.first(),
                nightMode = false,
                windowStart = 0,
                windowEnd = 24,
                state = ReserveUiState(studentName = "Asselay", startHour = 9, endHour = 11),
                onNameChange = {},
                onStartChange = {},
                onEndChange = {},
                onSubmit = {},
                onBack = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun ReserveSeatContentNightPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReserveSeatContent(
                seat = LibraryRepository.seats.first(),
                zone = LibraryRepository.zones.first(),
                nightMode = true,
                windowStart = 0,
                windowEnd = 6,
                state = ReserveUiState(studentName = "Asselay", startHour = 4, endHour = 6),
                onNameChange = {},
                onStartChange = {},
                onEndChange = {},
                onSubmit = {},
                onBack = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun ReserveSeatContentErrorPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReserveSeatContent(
                seat = LibraryRepository.seats.first(),
                zone = LibraryRepository.zones.first(),
                nightMode = false,
                windowStart = 0,
                windowEnd = 24,
                state = ReserveUiState(
                    studentName = "Asselay",
                    startHour = 1,
                    endHour = 3,
                    error = "Desk A1 is already reserved 00:00 – 02:00."
                ),
                onNameChange = {},
                onStartChange = {},
                onEndChange = {},
                onSubmit = {},
                onBack = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun ReserveSeatContentNotFoundPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReserveSeatContent(
                seat = null,
                zone = null,
                nightMode = false,
                windowStart = 0,
                windowEnd = 24,
                state = ReserveUiState(),
                onNameChange = {},
                onStartChange = {},
                onEndChange = {},
                onSubmit = {},
                onBack = {}
            )
        }
    }
}