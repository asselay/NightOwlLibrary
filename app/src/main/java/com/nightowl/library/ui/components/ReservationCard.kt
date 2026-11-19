package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.ui.theme.ComponentPreview
import com.nightowl.library.ui.theme.NightOwlLibraryTheme

@Composable
fun ReservationCard(
    reservation: Reservation,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "${reservation.seatName}  •  ${reservation.zoneName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reservation.timeRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Reserved for ${reservation.studentName}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onCancel) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Cancel reservation")
            }
        }
    }
}

@ComponentPreview
@Composable
private fun ReservationCardPreview() {
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReservationCard(
                    reservation = Reservation(1, 1, "Desk A1", "Silent Hall", "Asselay", 0, 2),
                    onCancel = {}
                )
                ReservationCard(
                    reservation = Reservation(2, 7, "Pod P1", "Study Pods", "Asselay", 3, 6),
                    onCancel = {}
                )
            }
        }
    }
}