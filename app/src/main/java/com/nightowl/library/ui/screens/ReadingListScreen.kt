package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.ReadingStatus
import com.nightowl.library.data.model.SavedBook
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EditSavedBookDialog
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.SavedBookCard
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.ReadingListViewModel

@Composable
fun ReadingListScreen(
    viewModel: ReadingListViewModel = viewModel(factory = ReadingListViewModel.Factory)
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val editingBook by viewModel.editingBook.collectAsStateWithLifecycle()

    ReadingListContent(
        books = books,
        filter = filter,
        editingBook = editingBook,
        onFilterChange = viewModel::onFilterChange,
        onEdit = viewModel::startEditing,
        onDismissEdit = viewModel::stopEditing,
        onSaveEdit = viewModel::saveChanges,
        onDelete = viewModel::delete
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingListContent(
    books: List<SavedBook>,
    filter: ReadingStatus?,
    editingBook: SavedBook?,
    onFilterChange: (ReadingStatus?) -> Unit,
    onEdit: (SavedBook) -> Unit,
    onDismissEdit: () -> Unit,
    onSaveEdit: (SavedBook, ReadingStatus, String, Int) -> Unit,
    onDelete: (SavedBook) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "My reading list")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filter == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("All") }
                )
            }
            items(ReadingStatus.entries) { status ->
                FilterChip(
                    selected = filter == status,
                    onClick = { onFilterChange(status) },
                    label = { Text(status.label) }
                )
            }
        }

        if (books.isEmpty()) {
            EmptyState(
                message = if (filter == null) {
                    "Your reading list is empty. Save books from the Discover tab."
                } else {
                    "No books marked as \"${filter.label}\"."
                }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    SavedBookCard(
                        book = book,
                        onEdit = { onEdit(book) },
                        onDelete = { onDelete(book) }
                    )
                }
            }
        }
    }

    editingBook?.let { book ->
        EditSavedBookDialog(
            book = book,
            onDismiss = onDismissEdit,
            onSave = { status, note, rating -> onSaveEdit(book, status, note, rating) }
        )
    }
}

@PreviewLightDark
@Composable
private fun ReadingListPreview() {
    NightOwlPreview {
        ReadingListContent(
            books = PreviewData.savedBooks,
            filter = null,
            editingBook = null,
            onFilterChange = {},
            onEdit = {},
            onDismissEdit = {},
            onSaveEdit = { _, _, _, _ -> },
            onDelete = {}
        )
    }
}

@Preview(name = "Empty filter", showBackground = true)
@Composable
private fun ReadingListEmptyPreview() {
    NightOwlPreview {
        ReadingListContent(
            books = emptyList(),
            filter = ReadingStatus.FINISHED,
            editingBook = null,
            onFilterChange = {},
            onEdit = {},
            onDismissEdit = {},
            onSaveEdit = { _, _, _, _ -> },
            onDelete = {}
        )
    }
}