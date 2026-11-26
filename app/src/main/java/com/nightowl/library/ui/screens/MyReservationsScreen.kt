package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.ReservationSort
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.ReservationCard
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.ReservationsViewModel

@Composable
fun MyReservationsScreen(
    onEdit: (Reservation) -> Unit = {},
    viewModel: ReservationsViewModel = viewModel(factory = ReservationsViewModel.Factory)
) {
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()

    MyReservationsContent(
        reservations = reservations,
        sort = sort,
        onSortChange = viewModel::onSortChange,
        onEdit = onEdit,
        onCancel = viewModel::cancel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsContent(
    reservations: List<Reservation>,
    sort: ReservationSort,
    onSortChange: (ReservationSort) -> Unit,
    onEdit: (Reservation) -> Unit,
    onCancel: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "My reservations")

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Sort:", style = MaterialTheme.typography.titleSmall)
            ReservationSort.entries.forEach { option ->
                FilterChip(
                    selected = sort == option,
                    onClick = { onSortChange(option) },
                    label = { Text(option.label) }
                )
            }
        }

        if (reservations.isEmpty()) {
            EmptyState(message = "You have no reservations yet. Open the Zones tab to reserve a seat.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onEdit = { onEdit(reservation) },
                        onCancel = { onCancel(reservation.id) }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MyReservationsPreview() {
    NightOwlPreview {
        MyReservationsContent(
            reservations = PreviewData.reservations,
            sort = ReservationSort.BY_TIME,
            onSortChange = {},
            onEdit = {},
            onCancel = {}
        )
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun MyReservationsEmptyPreview() {
    NightOwlPreview {
        MyReservationsContent(emptyList(), ReservationSort.BY_ZONE, {}, {}, {})
    }
}