package com.nightowl.library.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nightowl.library.data.model.Reservation

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val zoneId: Int,
    val zoneName: String,
    val seat: String,
    val startHour: Int,
    val endHour: Int,
    val studentName: String,
    val createdAt: Long = System.currentTimeMillis()
)

fun ReservationEntity.toReservation(): Reservation = Reservation(
    id = id,
    zoneId = zoneId,
    zoneName = zoneName,
    seat = seat,
    startHour = startHour,
    endHour = endHour,
    studentName = studentName
)