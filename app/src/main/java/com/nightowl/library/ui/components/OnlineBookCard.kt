package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.OnlineBook
import com.nightowl.library.ui.theme.NightOwlPreview
import com.nightowl.library.ui.theme.PreviewData

@Composable
fun OnlineBookCard(
    book: OnlineBook,
    isSaved: Boolean,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BookCover(
                url = book.coverUrl,
                contentDescription = book.title,
                modifier = Modifier.size(width = 72.dp, height = 108.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "by ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = listOfNotNull(
                        book.firstPublishYear?.let { "First published $it" },
                        "${book.editionCount} editions"
                    ).joinToString("  •  "),
                    style = MaterialTheme.typography.bodySmall
                )
                if (isSaved) {
                    OutlinedButton(onClick = {}, enabled = false) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("In my list")
                    }
                } else {
                    Button(onClick = onSave) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save to my list")
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun OnlineBookCardPreview() {
    NightOwlPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OnlineBookCard(book = PreviewData.onlineBooks[0], isSaved = false, onSave = {})
            OnlineBookCard(book = PreviewData.onlineBooks[1], isSaved = true, onSave = {})
        }
    }
}