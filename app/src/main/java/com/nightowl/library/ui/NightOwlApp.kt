package com.nightowl.library.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nightowl.library.ui.navigation.AppNavHost
import com.nightowl.library.ui.navigation.Routes
import com.nightowl.library.ui.theme.NightOwlPreview

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomItems = listOf(
    BottomItem(Routes.CATALOG, "Catalog", Icons.Default.Home),
    BottomItem(Routes.DISCOVER, "Discover", Icons.Default.Search),
    BottomItem(Routes.ZONES, "Zones", Icons.Default.Place),
    BottomItem(Routes.MY_RESERVATIONS, "Bookings", Icons.Default.DateRange),
    BottomItem(Routes.READING_LIST, "My list", Icons.AutoMirrored.Filled.List)
)

@Composable
fun NightOwlApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomItems.any { it.route == currentRoute }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NightOwlBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .then(if (showBottomBar) Modifier else Modifier.navigationBarsPadding())
        )
    }
}

@Composable
private fun NightOwlBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun NightOwlBottomBarPreview() {
    NightOwlPreview {
        NightOwlBottomBar(currentRoute = Routes.DISCOVER, onNavigate = {})
    }
}