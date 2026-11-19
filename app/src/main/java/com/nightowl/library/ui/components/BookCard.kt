package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.theme.ComponentPreview
import com.nightowl.library.ui.theme.NightOwlLibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "by ${book.author}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${book.category}  •  ${book.aisle}, ${book.shelf}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = if (book.isAvailable) "Available" else "Borrowed",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BookCardPreview() {
    val books = LibraryRepository.books.value
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BookCard(book = books.first(), onClick = {})
                BookCard(book = books.last(), onClick = {})
            }
        }
    }
}