package com.nightowl.library.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nightowl.library.ui.theme.NightOwlPreview

@Composable
fun HourStepper(
    label: String,
    hour: Int,
    onHourChange: (Int) -> Unit,
    minHour: Int,
    maxHour: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(onClick = { if (hour > minHour) onHourChange(hour - 1) }) {
            Text("-")
        }
        Text(
            text = "%02d:00".format(hour),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(onClick = { if (hour < maxHour) onHourChange(hour + 1) }) {
            Text("+")
        }
    }
}

@PreviewLightDark
@Composable
private fun HourStepperPreview() {
    NightOwlPreview {
        HourStepper(
            label = "Start",
            hour = 2,
            onHourChange = {},
            minHour = 0,
            maxHour = 23,
            modifier = Modifier.padding(16.dp)
        )
    }
}