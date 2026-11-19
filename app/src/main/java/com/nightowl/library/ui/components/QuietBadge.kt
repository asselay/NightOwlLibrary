package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightowl.library.data.model.QuietLevel
import com.nightowl.library.ui.theme.ComponentPreview
import com.nightowl.library.ui.theme.LevelBusy
import com.nightowl.library.ui.theme.LevelModerate
import com.nightowl.library.ui.theme.LevelQuiet
import com.nightowl.library.ui.theme.LevelSilent
import com.nightowl.library.ui.theme.NightOwlLibraryTheme
import com.nightowl.library.ui.theme.White

@Composable
fun QuietBadge(level: QuietLevel, modifier: Modifier = Modifier) {
    val color = when (level) {
        QuietLevel.SILENT -> LevelSilent
        QuietLevel.QUIET -> LevelQuiet
        QuietLevel.MODERATE -> LevelModerate
        QuietLevel.BUSY -> LevelBusy
    }
    Surface(
        color = color,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = level.label,
            color = White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@ComponentPreview
@Composable
private fun QuietBadgePreview() {
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuietLevel.entries.forEach { QuietBadge(level = it) }
            }
        }
    }
}