package com.nightowl.library.data.model

data class Reservation(
    val id: Int,
    val seatId: Int,
    val seatName: String,
    val zoneName: String,
    val studentName: String,
    val startHour: Int,
    val endHour: Int
) {
    val timeRange: String
        get() = "${formatHour(startHour)} – ${formatHour(endHour)}"

    fun overlaps(start: Int, end: Int): Boolean = startHour < end && start < endHour
}

fun formatHour(hour: Int): String = "%02d:00".format(hour)