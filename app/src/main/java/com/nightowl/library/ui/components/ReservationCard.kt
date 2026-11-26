package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData

@Composable
fun ReservationCard(
    reservation: Reservation,
    modifier: Modifier = Modifier,
    onEdit: (() -> Unit)? = null,
    onCancel: () -> Unit = {}
) {
    val isNightSlot = reservation.startHour < LibraryRepository.NIGHT_END_HOUR

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${reservation.zoneName} - ${reservation.seatName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reservation.timeRange,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Reserved for ${reservation.studentName}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (isNightSlot) {
                    Text(
                        text = "Night slot",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit reservation")
                }
            }
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Cancel reservation",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ReservationCardPreview() {
    NightOwlPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReservationCard(
                reservation = PreviewData.reservations[0],
                onEdit = {},
                onCancel = {}
            )
            ReservationCard(
                reservation = PreviewData.reservations[1],
                onCancel = {}
            )
        }
    }
}