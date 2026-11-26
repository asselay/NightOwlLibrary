package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.ui.theme.NightOwlPreview

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    onRatingChange: ((Int) -> Unit)? = null
) {
    Row(modifier = modifier) {
        for (value in 1..maxRating) {
            val tint = if (value <= rating) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
            if (onRatingChange != null) {
                IconButton(onClick = { onRatingChange(if (value == rating) 0 else value) }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "$value stars",
                        tint = tint
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RatingBarPreview() {
    NightOwlPreview {
        Column(modifier = Modifier.padding(16.dp)) {
            RatingBar(rating = 3)
            RatingBar(rating = 4, onRatingChange = {})
        }
    }
}