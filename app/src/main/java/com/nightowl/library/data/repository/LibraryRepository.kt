package com.nightowl.library.data.repository

import com.nightowl.library.data.model.Book
import com.nightowl.library.data.model.QuietLevel
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone
import com.nightowl.library.data.model.formatHour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object LibraryRepository {

    const val DAY_START_HOUR = 0
    const val DAY_END_HOUR = 24
    const val NIGHT_START_HOUR = 0
    const val NIGHT_END_HOUR = 6

    private val _books = MutableStateFlow(
        listOf(
            Book(1, "Clean Code", "Robert C. Martin", "Programming",
                "A practical guide to writing readable and maintainable code.",
                "Aisle A", "Shelf 2"),
            Book(2, "Kotlin in Action", "Dmitry Jemerov", "Programming",
                "A complete guide to Kotlin for Java and Android developers.",
                "Aisle A", "Shelf 4"),
            Book(3, "Deep Work", "Cal Newport", "Self-development",
                "Rules for focused success in a distracted world.",
                "Aisle B", "Shelf 1"),
            Book(4, "Atomic Habits", "James Clear", "Self-development",
                "How tiny daily changes build remarkable long-term results.",
                "Aisle B", "Shelf 3"),
            Book(5, "The Pragmatic Programmer", "Andrew Hunt", "Programming",
                "Tips and habits that help developers grow into better engineers.",
                "Aisle C", "Shelf 2", false),
            Book(6, "Introduction to Algorithms", "Thomas H. Cormen", "Computer Science",
                "A comprehensive textbook covering modern algorithms and data structures.",
                "Aisle C", "Shelf 5", false)
        )
    )
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    val zones: List<Zone> = listOf(
        Zone(1, "Silent Hall", QuietLevel.SILENT, true),
        Zone(2, "Window Row", QuietLevel.QUIET, true),
        Zone(3, "Study Pods", QuietLevel.SILENT, true),
        Zone(4, "Group Area", QuietLevel.BUSY, false),
        Zone(5, "Coffee Corner", QuietLevel.MODERATE, false)
    )

    val seats: List<Seat> = listOf(
        Seat(1, "Desk A1", 1),
        Seat(2, "Desk A2", 1),
        Seat(3, "Desk A3", 1),
        Seat(4, "Desk B1", 2),
        Seat(5, "Desk B2", 2),
        Seat(6, "Desk B3", 2),
        Seat(7, "Pod P1", 3),
        Seat(8, "Pod P2", 3),
        Seat(9, "Table G1", 4),
        Seat(10, "Table G2", 4),
        Seat(11, "Table G3", 4),
        Seat(12, "Sofa C1", 5),
        Seat(13, "Sofa C2", 5)
    )

    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations.asStateFlow()

    private val _nightMode = MutableStateFlow(false)
    val nightMode: StateFlow<Boolean> = _nightMode.asStateFlow()

    private var nextReservationId = 1

    fun getBook(id: Int): Book? = _books.value.firstOrNull { it.id == id }

    fun getZone(id: Int): Zone? = zones.firstOrNull { it.id == id }

    fun getSeat(id: Int): Seat? = seats.firstOrNull { it.id == id }

    fun windowStart(): Int = if (_nightMode.value) NIGHT_START_HOUR else DAY_START_HOUR

    fun windowEnd(): Int = if (_nightMode.value) NIGHT_END_HOUR else DAY_END_HOUR

    fun setNightMode(enabled: Boolean) {
        _nightMode.value = enabled
    }

    fun freeSeats(
        zoneId: Int,
        start: Int,
        end: Int,
        current: List<Reservation> = _reservations.value
    ): List<Seat> = seats.filter { seat ->
        seat.zoneId == zoneId && (start until end).any { hour ->
            current.none { it.seatId == seat.id && hour >= it.startHour && hour < it.endHour }
        }
    }

    fun reserve(seatId: Int, studentName: String, startHour: Int, endHour: Int): String? {
        val seat = getSeat(seatId) ?: return "Seat not found."
        val zone = getZone(seat.zoneId) ?: return "Zone not found."
        if (studentName.isBlank()) return "Please enter your name."
        if (endHour <= startHour) return "The end time must be after the start time."
        if (startHour < windowStart() || endHour > windowEnd()) {
            return "Choose a time between ${formatHour(windowStart())} and ${formatHour(windowEnd())}."
        }
        if (_nightMode.value && !zone.openAtNight) return "${zone.name} is closed at night."
        val conflict = _reservations.value.firstOrNull {
            it.seatId == seatId && it.overlaps(startHour, endHour)
        }
        if (conflict != null) return "${seat.name} is already reserved ${conflict.timeRange}."
        _reservations.update {
            it + Reservation(nextReservationId++, seat.id, seat.name, zone.name, studentName.trim(), startHour, endHour)
        }
        return null
    }

    fun cancelReservation(id: Int) {
        _reservations.update { list -> list.filterNot { it.id == id } }
    }
}