package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.ZoneWithSeats
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.NightModeToggle
import com.nightowl.library.ui.components.ZoneCard
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.ScreenPreview
import com.nightowl.library.viewmodel.ZonesViewModel

@Composable
fun SeatMapScreen(
    onSeatClick: (Int) -> Unit,
    viewModel: ZonesViewModel = viewModel()
) {
    val zones by viewModel.zones.collectAsStateWithLifecycle()
    val nightMode by viewModel.nightMode.collectAsStateWithLifecycle()

    SeatMapContent(
        zones = zones,
        nightMode = nightMode,
        onNightModeChange = viewModel::onNightModeChange,
        onSeatClick = onSeatClick
    )
}

@Composable
private fun SeatMapContent(
    zones: List<ZoneWithSeats>,
    nightMode: Boolean,
    onNightModeChange: (Boolean) -> Unit,
    onSeatClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Seat map")

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NightModeToggle(enabled = nightMode, onChange = onNightModeChange)
            Text(
                text = if (nightMode) {
                    "Zones open between 00:00 and 06:00, with seats that have free time."
                } else {
                    "All zones, with seats that have free time today."
                },
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (zones.isEmpty()) {
            EmptyState(message = "No zones are available.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(zones, key = { it.zone.id }) { entry ->
                    ZoneCard(entry = entry, onSeatClick = onSeatClick)
                }
            }
        }
    }
}

private fun sampleZones(): List<ZoneWithSeats> =
    LibraryRepository.zones.map { zone ->
        val seats = LibraryRepository.seats.filter { it.zoneId == zone.id }
        ZoneWithSeats(zone, seats, seats.size)
    }

@ScreenPreview
@Composable
private fun SeatMapContentPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeatMapContent(
                zones = sampleZones(),
                nightMode = false,
                onNightModeChange = {},
                onSeatClick = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun SeatMapContentNightPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeatMapContent(
                zones = sampleZones().filter { it.zone.openAtNight },
                nightMode = true,
                onNightModeChange = {},
                onSeatClick = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun SeatMapContentEmptyPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SeatMapContent(
                zones = emptyList(),
                nightMode = true,
                onNightModeChange = {},
                onSeatClick = {}
            )
        }
    }
}