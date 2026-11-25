package com.nightowl.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.ui.components.AppTopBar
import com.nightowl.library.ui.components.EmptyState
import com.nightowl.library.ui.components.ErrorState
import com.nightowl.library.ui.components.LoadingState
import com.nightowl.library.ui.components.OnlineBookCard
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData
import com.nightowl.library.viewmodel.DiscoverUiState
import com.nightowl.library.viewmodel.DiscoverViewModel

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = viewModel(factory = DiscoverViewModel.Factory)
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savedKeys by viewModel.savedKeys.collectAsStateWithLifecycle()

    DiscoverContent(
        query = query,
        uiState = uiState,
        savedKeys = savedKeys,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onRetry = viewModel::retry,
        onSave = viewModel::saveBook
    )
}

@Composable
fun DiscoverContent(
    query: String,
    uiState: DiscoverUiState,
    savedKeys: Set<String>,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onSave: (OnlineBook) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val submitSearch = {
        keyboardController?.hide()
        onSearch()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(title = "Discover online")

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Search Open Library") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = submitSearch) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { submitSearch() }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when (uiState) {
            DiscoverUiState.Loading -> LoadingState(message = "Loading books from Open Library...")

            is DiscoverUiState.Success -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.books, key = { it.workKey }) { book ->
                    OnlineBookCard(
                        book = book,
                        isSaved = book.workKey in savedKeys,
                        onSave = { onSave(book) }
                    )
                }
            }

            is DiscoverUiState.Error -> ErrorState(message = uiState.message, onRetry = onRetry)

            DiscoverUiState.Empty -> EmptyState(
                message = if (query.isBlank()) {
                    "Type a title or author to search."
                } else {
                    "No books found for \"$query\". Try another search."
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DiscoverSuccessPreview() {
    NightOwlPreview {
        DiscoverContent(
            query = "programming",
            uiState = DiscoverUiState.Success(PreviewData.onlineBooks),
            savedKeys = setOf(PreviewData.onlineBooks[1].workKey),
            onQueryChange = {},
            onSearch = {},
            onRetry = {},
            onSave = {}
        )
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun DiscoverLoadingPreview() {
    NightOwlPreview {
        DiscoverContent("programming", DiscoverUiState.Loading, emptySet(), {}, {}, {}, {})
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun DiscoverErrorPreview() {
    NightOwlPreview {
        DiscoverContent(
            "programming",
            DiscoverUiState.Error("No internet connection. Check your network and try again."),
            emptySet(), {}, {}, {}, {}
        )
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun DiscoverEmptyPreview() {
    NightOwlPreview {
        DiscoverContent("qwertyzzz", DiscoverUiState.Empty, emptySet(), {}, {}, {}, {})
    }
}