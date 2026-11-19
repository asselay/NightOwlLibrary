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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import com.nightowl.library.ui.components.BookCard
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.ScreenPreview
import com.nightowl.library.viewmodel.CatalogViewModel

@Composable
fun CatalogScreen(
    onBookClick: (Int) -> Unit,
    viewModel: CatalogViewModel = viewModel()
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    CatalogContent(
        books = books,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onBookClick = onBookClick
    )
}

@Composable
private fun CatalogContent(
    books: List<Book>,
    query: String,
    onQueryChange: (String) -> Unit,
    onBookClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Book catalog")

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Search title, author or category") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

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

@ScreenPreview
@Composable
private fun CatalogContentPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CatalogContent(
                books = LibraryRepository.books.value,
                query = "",
                onQueryChange = {},
                onBookClick = {}
            )
        }
    }
}

@ScreenPreview
@Composable
private fun CatalogContentEmptyPreview() {
    NightOwlLibraryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CatalogContent(
                books = emptyList(),
                query = "xyz",
                onQueryChange = {},
                onBookClick = {}
            )
        }
    }
}