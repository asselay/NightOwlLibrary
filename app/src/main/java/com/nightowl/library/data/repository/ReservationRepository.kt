package com.nightowl.library.data.repository

import com.nightowl.library.data.local.ReservationDao
import com.nightowl.library.data.local.ReservationEntity
import com.nightowl.library.data.local.toReservation
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReservationRepository(private val dao: ReservationDao) {

    fun observeReservations(): Flow<List<Reservation>> =
        dao.observeAll().map { list -> list.map { it.toReservation() } }

    suspend fun getReservation(id: Int): Reservation? = dao.getById(id)?.toReservation()

    suspend fun findConflict(
        seatId: Int,
        startHour: Int,
        endHour: Int,
        excludeId: Int = 0
    ): Reservation? = dao.findConflict(seatId, startHour, endHour, excludeId)?.toReservation()

    suspend fun addReservation(
        seat: Seat,
        zone: Zone,
        studentName: String,
        startHour: Int,
        endHour: Int
    ) {
        dao.insert(
            ReservationEntity(
                seatId = seat.id,
                seatName = seat.name,
                zoneName = zone.name,
                studentName = studentName,
                startHour = startHour,
                endHour = endHour
            )
        )
    }

    suspend fun updateReservation(reservation: Reservation) {
        val existing = dao.getById(reservation.id) ?: return
        dao.update(
            existing.copy(
                seatId = reservation.seatId,
                seatName = reservation.seatName,
                zoneName = reservation.zoneName,
                studentName = reservation.studentName,
                startHour = reservation.startHour,
                endHour = reservation.endHour
            )
        )
    }

    suspend fun deleteReservation(id: Int) {
        dao.deleteById(id)
    }
}