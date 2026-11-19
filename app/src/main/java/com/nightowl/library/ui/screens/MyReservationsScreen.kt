package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.ReservationCard
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.ScreenPreview
import com.nightowl.library.viewmodel.ReservationsViewModel

@Composable
fun MyReservationsScreen(
    viewModel: ReservationsViewModel = viewModel()
) {
    val reservations by viewModel.reservations.collectAsStateWithLifecycle()

    MyReservationsContent(
        reservations = reservations,
        onCancel = viewModel::cancel
    )
}

@Composable
private fun MyReservationsContent(
    reservations: List<Reservation>,
    onCancel: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "My reservations")

        if (reservations.isEmpty()) {
            EmptyState(message = "You have no reservations yet. Open the Seats tab to reserve one.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    ReservationCard(
                        reservation = reservation,
                        onCancel = { onCancel(reservation.id) }
                    )
                }
            }
        }
    }
}

@ScreenPreview
@Composable
private fun MyReservationsContentPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyReservationsContent(
                reservations = listOf(
                    Reservation(1, 1, "Desk A1", "Silent Hall", "Asselay", 0, 2),
                    Reservation(2, 7, "Pod P1", "Study Pods", "Asselay", 3, 6)
                ),
                onCancel = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun MyReservationsContentEmptyPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyReservationsContent(
                reservations = emptyList(),
                onCancel = {}
            )
        }
    }
}