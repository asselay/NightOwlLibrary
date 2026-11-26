package com.nightowl.library.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.QuietBadge
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.ZoneUi
import com.nightowl.library.viewmodel.ZonesViewModel

@Composable
fun SeatMapScreen(
    onReserve: (Int) -> Unit,
    viewModel: ZonesViewModel = viewModel(factory = ZonesViewModel.Factory)
) {
    val zones by viewModel.zones.collectAsStateWithLifecycle()
    val nightMode by viewModel.nightMode.collectAsStateWithLifecycle()

    SeatMapContent(
        zones = zones,
        nightMode = nightMode,
        onNightModeChange = viewModel::onNightModeChange,
        onReserve = onReserve
    )
}

@Composable
fun SeatMapContent(
    zones: List<ZoneUi>,
    nightMode: Boolean,
    onNightModeChange: (Boolean) -> Unit,
    onReserve: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Seat map & zones")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Night mode", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Show only zones open 00:00 - 06:00",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(checked = nightMode, onCheckedChange = onNightModeChange)
            }
        }

        if (zones.isEmpty()) {
            EmptyState(message = "No zones are open right now.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(zones, key = { it.zone.id }) { zoneUi ->
                    ZoneSeatsCard(zoneUi = zoneUi, onSeatClick = onReserve)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ZoneSeatsCard(
    zoneUi: ZoneUi,
    onSeatClick: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = zoneUi.zone.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                QuietBadge(level = zoneUi.zone.quietLevel)
            }
            Text(
                text = "Free seats: ${zoneUi.freeSeats} / ${zoneUi.totalSeats}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (zoneUi.zone.openAtNight) {
                Text(
                    text = "Open at night (00:00 - 06:00)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                zoneUi.seats.forEach { seat ->
                    FilterChip(
                        selected = false,
                        enabled = seat.id in zoneUi.freeSeatIds,
                        onClick = { onSeatClick(seat.id) },
                        label = { Text(seat.name) }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SeatMapPreview() {
    NightOwlPreview {
        SeatMapContent(
            zones = listOf(
                ZoneUi(PreviewData.zones[0], PreviewData.seats, setOf(1, 3)),
                ZoneUi(PreviewData.zones[1], emptyList(), emptySet())
            ),
            nightMode = false,
            onNightModeChange = {},
            onReserve = {}
        )
    }
}