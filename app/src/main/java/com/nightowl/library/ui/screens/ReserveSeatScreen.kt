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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.model.formatHour
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.QuietBadge
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.ReserveUiState
import com.nightowl.library.viewmodel.ReserveViewModel

@Composable
fun ReserveSeatScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ReserveViewModel = viewModel(factory = ReserveViewModel.Factory)
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
fun ReserveSeatContent(
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
        AppTopBar(
            title = if (state.isEditMode) "Edit reservation" else "Reserve seat",
            onBack = onBack
        )

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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = seat.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = zone.name, style = MaterialTheme.typography.bodyMedium)
                    }
                    QuietBadge(level = zone.quietLevel)
                }

                Text(
                    text = if (nightMode) {
                        "Night mode: choose a time between ${formatHour(windowStart)} and ${formatHour(windowEnd)}"
                    } else {
                        "Available ${formatHour(windowStart)} – ${formatHour(windowEnd)}"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = state.studentName,
                    onValueChange = onNameChange,
                    label = { Text("Your name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                TimeStepper(
                    label = "Start",
                    hour = state.startHour,
                    onMinus = { onStartChange(-1) },
                    onPlus = { onStartChange(1) }
                )
                TimeStepper(
                    label = "End",
                    hour = state.endHour,
                    onMinus = { onEndChange(-1) },
                    onPlus = { onEndChange(1) }
                )

                state.error?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = onSubmit,
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isEditMode) "Save changes" else "Confirm reservation")
                }
            }
        }
    }
}

@Composable
private fun TimeStepper(
    label: String,
    hour: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = onMinus) { Text("-") }
        Text(
            text = formatHour(hour),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(onClick = onPlus) { Text("+") }
    }
}

@PreviewLightDark
@Composable
private fun ReserveSeatPreview() {
    NightOwlPreview {
        ReserveSeatContent(
            seat = PreviewData.seats.first(),
            zone = PreviewData.zones.first(),
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

@Preview(name = "Edit with error", showBackground = true)
@Composable
private fun ReserveSeatErrorPreview() {
    NightOwlPreview {
        ReserveSeatContent(
            seat = PreviewData.seats.first(),
            zone = PreviewData.zones.first(),
            nightMode = true,
            windowStart = 0,
            windowEnd = 6,
            state = ReserveUiState(
                studentName = "Asselay",
                startHour = 1,
                endHour = 4,
                isEditMode = true,
                error = "Desk A1 is already reserved 02:00 – 03:00."
            ),
            onNameChange = {},
            onStartChange = {},
            onEndChange = {},
            onSubmit = {},
            onBack = {}
        )
    }
}