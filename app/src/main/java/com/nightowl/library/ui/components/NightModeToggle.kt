package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.ui.theme.NightOwlPreview

@Composable
fun NightModeToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Night mode", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Show only what is available 00:00 - 06:00",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}

@PreviewLightDark
@Composable
private fun NightModeTogglePreview() {
    NightOwlPreview {
        NightModeToggle(
            checked = true,
            onCheckedChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}