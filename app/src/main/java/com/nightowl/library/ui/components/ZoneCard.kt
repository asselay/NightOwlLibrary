package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.ZoneWithSeats
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.theme.ComponentPreview
import com.nightowl.library.ui.theme.NightOwlLibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoneCard(
    entry: ZoneWithSeats,
    onSeatClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.zone.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                QuietBadge(level = entry.zone.quietLevel)
            }
            Text(
                text = "Free seats: ${entry.freeSeats.size} of ${entry.totalSeats}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (entry.freeSeats.isEmpty()) {
                Text(
                    text = "No free seats in this time window.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entry.freeSeats, key = { it.id }) { seat ->
                        AssistChip(
                            onClick = { onSeatClick(seat.id) },
                            label = { Text(seat.name) }
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ZoneCardPreview() {
    val zone = LibraryRepository.zones.first()
    val seats = LibraryRepository.seats.filter { it.zoneId == zone.id }
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ZoneCard(entry = ZoneWithSeats(zone, seats, seats.size), onSeatClick = {})
                ZoneCard(entry = ZoneWithSeats(zone, seats.take(1), seats.size), onSeatClick = {})
                ZoneCard(entry = ZoneWithSeats(zone, emptyList(), seats.size), onSeatClick = {})
            }
        }
    }
}