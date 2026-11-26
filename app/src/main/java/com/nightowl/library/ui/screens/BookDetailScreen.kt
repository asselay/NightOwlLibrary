package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Book
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.BookDetailViewModel

@Composable
fun BookDetailScreen(
    onBack: () -> Unit,
    onFindSeat: (() -> Unit)? = null,
    viewModel: BookDetailViewModel = viewModel()
) {
    BookDetailContent(
        book = viewModel.book,
        onBack = onBack,
        onFindSeat = onFindSeat
    )
}

@Composable
fun BookDetailContent(
    book: Book?,
    onBack: () -> Unit,
    onFindSeat: (() -> Unit)?
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
                Text(text = "Category: ${book.category}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Location: ${book.aisle}, ${book.shelf}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (book.isAvailable) "Available now" else "Currently checked out",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (book.isAvailable) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                HorizontalDivider()
                Text(text = book.description, style = MaterialTheme.typography.bodyLarge)
                if (onFindSeat != null) {
                    Button(onClick = onFindSeat, modifier = Modifier.fillMaxWidth()) {
                        Text("Find a study seat")
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun BookDetailPreview() {
    NightOwlPreview {
        BookDetailContent(
            book = PreviewData.books[0],
            onBack = {},
            onFindSeat = {}
        )
    }
}

@Preview(name = "Checked out", showBackground = true)
@Composable
private fun BookDetailCheckedOutPreview() {
    NightOwlPreview {
        BookDetailContent(
            book = PreviewData.books[1],
            onBack = {},
            onFindSeat = null
        )
    }
}