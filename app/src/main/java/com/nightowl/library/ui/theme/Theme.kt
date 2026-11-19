package com.nightowl.library.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val DarkColors = darkColorScheme(
    primary = Amber,
    onPrimary = Navy,
    secondary = Cream,
    onSecondary = Navy,
    background = Navy,
    onBackground = Cream,
    surface = NavySurface,
    onSurface = Cream,
    surfaceVariant = NavySurface,
    onSurfaceVariant = Cream
)

private val LightColors = lightColorScheme(
    primary = AmberDark,
    onPrimary = White,
    secondary = Navy,
    onSecondary = White,
    background = Cream,
    onBackground = Navy,
    surface = White,
    onSurface = Navy,
    surfaceVariant = Cream,
    onSurfaceVariant = Navy
)

@Composable
fun NightOwlLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}

@ComponentPreview
@Composable
private fun ThemePreview() {
    NightOwlLibraryTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("NightOwl Library", style = MaterialTheme.typography.titleLarge)
                Text("Background, surface and text colors", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = {}) { Text("Primary button") }
            }
        }
    }
}