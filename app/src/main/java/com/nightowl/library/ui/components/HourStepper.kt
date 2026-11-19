package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightowl.library.ui.theme.ComponentPreview
import com.nightowl.library.ui.theme.NightOwlLibraryTheme

@Composable
fun HourStepper(
    label: String,
    value: String,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier,
    canDecrease: Boolean = true,
    canIncrease: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = onDecrease, enabled = canDecrease) { Text("-") }
        Text(text = value, style = MaterialTheme.typography.titleLarge)
        OutlinedButton(onClick = onIncrease, enabled = canIncrease) { Text("+") }
    }
}

@ComponentPreview
@Composable
private fun HourStepperPreview() {
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HourStepper(label = "From", value = "02:00", onDecrease = {}, onIncrease = {})
                HourStepper(
                    label = "To",
                    value = "06:00",
                    onDecrease = {},
                    onIncrease = {},
                    canIncrease = false
                )
            }
        }
    }
}