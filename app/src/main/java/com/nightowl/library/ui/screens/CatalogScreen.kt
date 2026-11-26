package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.Book
import com.nightowl.library.data.repository.LibraryRepository
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.BookCard
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.NightModeToggle
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(
    onBookClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: CatalogViewModel = viewModel()
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val nightMode by LibraryRepository.nightMode.collectAsStateWithLifecycle()

    CatalogContent(
        books = books,
        query = query,
        nightMode = nightMode,
        onQueryChange = viewModel::onQueryChange,
        onNightModeChange = LibraryRepository::setNightMode,
        onBookClick = onBookClick,
        onSettingsClick = onSettingsClick
    )
}

@Composable
fun CatalogContent(
    books: List<Book>,
    query: String,
    nightMode: Boolean,
    onQueryChange: (String) -> Unit,
    onNightModeChange: (Boolean) -> Unit,
    onBookClick: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = "NightOwl Library",
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search title or author") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            NightModeToggle(
                checked = nightMode,
                onCheckedChange = onNightModeChange
            )
        }

        if (books.isEmpty()) {
            EmptyState(message = "No books match your search.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    BookCard(book = book, onClick = { onBookClick(book.id) })
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CatalogPreview() {
    NightOwlPreview {
        CatalogContent(
            books = PreviewData.books,
            query = "",
            nightMode = false,
            onQueryChange = {},
            onNightModeChange = {},
            onBookClick = {},
            onSettingsClick = {}
        )
    }
}