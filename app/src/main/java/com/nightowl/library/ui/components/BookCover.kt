package com.nightowl.library.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nightowl.library.ui.theme.NightOwlPreview

@Composable
fun BookCover(
    url: String?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val placeholderColor = MaterialTheme.colorScheme.surfaceVariant
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(placeholderColor),
        contentAlignment = Alignment.Center
    ) {
        if (url == null) {
            Text(
                text = "No cover",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp)
            )
        } else {
            AsyncImage(
                model = url,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(placeholderColor),
                error = ColorPainter(placeholderColor),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun BookCoverPreview() {
    NightOwlPreview {
        BookCover(
            url = null,
            contentDescription = "Cover",
            modifier = Modifier
                .padding(16.dp)
                .size(width = 72.dp, height = 108.dp)
        )
    }
}