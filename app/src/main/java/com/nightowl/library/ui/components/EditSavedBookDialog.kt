package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.SavedBook
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData

@Composable
fun EditSavedBookDialog(
    book: SavedBook,
    onDismiss: () -> Unit,
    onSave: (ReadingStatus, String, Int) -> Unit
) {
    var status by rememberSaveable(book.id) { mutableStateOf(book.status) }
    var note by rememberSaveable(book.id) { mutableStateOf(book.note) }
    var rating by rememberSaveable(book.id) { mutableIntStateOf(book.rating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = book.title, maxLines = 2, overflow = TextOverflow.Ellipsis)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Status", style = MaterialTheme.typography.titleSmall)
                ReadingStatus.entries.forEach { option ->
                    RadioOptionRow(
                        label = option.label,
                        selected = status == option,
                        onClick = { status = option }
                    )
                }
                Text(text = "Rating", style = MaterialTheme.typography.titleSmall)
                RatingBar(rating = rating, onRatingChange = { rating = it })
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Personal note") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(status, note, rating) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@PreviewLightDark
@Composable
private fun EditSavedBookDialogPreview() {
    NightOwlPreview {
        EditSavedBookDialog(
            book = PreviewData.savedBooks.first(),
            onDismiss = {},
            onSave = { _, _, _ -> }
        )
    }
}