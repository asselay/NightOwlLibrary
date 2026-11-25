package com.nightowl.library.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.data.model.QuietLevel
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.Reservation
import com.nightowl.library.data.model.SavedBook
import com.nightowl.library.data.model.Seat
import com.nightowl.library.data.model.Zone

@Composable
fun NightOwlPreview(content: @Composable () -> Unit) {
    NightOwlLibraryTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

object PreviewData {

    val books = listOf(
        Book(1, "Clean Code", "Robert C. Martin", "Programming",
            "A practical guide to writing readable and maintainable code.",
            "Aisle A", "Shelf 2"),
        Book(5, "The Pragmatic Programmer", "Andrew Hunt", "Programming",
            "Tips and habits that help developers grow into better engineers.",
            "Aisle C", "Shelf 2", false)
    )

    val zones = listOf(
        Zone(1, "Silent Hall", QuietLevel.SILENT, true),
        Zone(4, "Group Area", QuietLevel.BUSY, false)
    )

    val seats = listOf(
        Seat(1, "Desk A1", 1),
        Seat(2, "Desk A2", 1),
        Seat(3, "Desk A3", 1)
    )

    val reservations = listOf(
        Reservation(1, 1, "Desk A1", "Silent Hall", "Asselay", 1, 4),
        Reservation(2, 7, "Pod P1", "Study Pods", "Asselay", 14, 16)
    )

    val onlineBooks = listOf(
        OnlineBook("/works/OL1W", "The Pragmatic Programmer", "Andrew Hunt", 1999, null, 25),
        OnlineBook("/works/OL2W", "Structure and Interpretation of Computer Programs",
            "Harold Abelson", 1985, null, 40)
    )

    val savedBooks = listOf(
        SavedBook(1, "/works/OL1W", "The Pragmatic Programmer", "Andrew Hunt", null, 1999,
            ReadingStatus.READING, "Great tips about writing clean code.", 4, 0L),
        SavedBook(2, "/works/OL3W", "Dune", "Frank Herbert", null, 1965,
            ReadingStatus.WANT_TO_READ, "", 0, 0L)
    )
}

@PreviewLightDark
@Composable
private fun NightOwlPreviewSample() {
    NightOwlPreview {
        Text(
            text = "NightOwl Library preview theme",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}