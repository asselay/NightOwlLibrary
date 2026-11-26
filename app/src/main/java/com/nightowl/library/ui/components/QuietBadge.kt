package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.QuietLevel
import com.nightowl.library.ui.theme.NightOwlPreview

private val SilentColor = Color(0xFF2E7D32)
private val QuietColor = Color(0xFF0277BD)
private val ModerateColor = Color(0xFFEF6C00)
private val BusyColor = Color(0xFFC62828)

@Composable
fun QuietBadge(level: QuietLevel, modifier: Modifier = Modifier) {
    val color = when (level) {
        QuietLevel.SILENT -> SilentColor
        QuietLevel.QUIET -> QuietColor
        QuietLevel.MODERATE -> ModerateColor
        QuietLevel.BUSY -> BusyColor
    }
    val text = when (level) {
        QuietLevel.SILENT -> "Silent"
        QuietLevel.QUIET -> "Quiet"
        QuietLevel.MODERATE -> "Moderate"
        QuietLevel.BUSY -> "Busy"
    }
    Surface(
        color = color,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun QuietBadgePreview() {
    NightOwlPreview {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuietLevel.entries.forEach { QuietBadge(level = it) }
        }
    }
}