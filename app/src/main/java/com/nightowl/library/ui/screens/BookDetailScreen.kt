package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.ScreenPreview
import com.nightowl.library.viewmodel.BookDetailViewModel

@Composable
fun BookDetailScreen(
    onBack: () -> Unit,
    viewModel: BookDetailViewModel = viewModel()
) {
    val book by viewModel.book.collectAsStateWithLifecycle()

    BookDetailContent(book = book, onBack = onBack)
}

@Composable
private fun BookDetailContent(
    book: Book?,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Book details", onBack = onBack)

        if (book == null) {
            EmptyState(message = "Book not found.")
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = book.title, style = MaterialTheme.typography.headlineSmall)
                Text(text = "by ${book.author}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = book.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = book.description, style = MaterialTheme.typography.bodyLarge)

                Text(text = "Where to find it", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${book.aisle}  •  ${book.shelf}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (book.isAvailable) {
                        "Available now. You can take it from the shelf."
                    } else {
                        "Currently borrowed. Check again later."
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@ScreenPreview
@Composable
private fun BookDetailContentPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BookDetailContent(
                book = LibraryRepository.books.value.first(),
                onBack = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun BookDetailContentBorrowedPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BookDetailContent(
                book = LibraryRepository.books.value.last(),
                onBack = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun BookDetailContentNotFoundPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            BookDetailContent(
                book = null,
                onBack = {}
            )
        }
    }
}