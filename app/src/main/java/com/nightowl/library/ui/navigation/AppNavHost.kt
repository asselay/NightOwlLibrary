package com.nightowl.library.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nightowl.library.ui.screens.BookDetailScreen
import com.nightowl.library.ui.screens.CatalogScreen
import com.nightowl.library.ui.screens.DiscoverScreen
import com.nightowl.library.ui.screens.MyReservationsScreen
import com.nightowl.library.ui.screens.ReadingListScreen
import com.nightowl.library.ui.screens.ReserveSeatScreen
import com.nightowl.library.ui.screens.SeatMapScreen
import com.nightowl.library.ui.screens.SettingsScreen
import com.nightowl.library.ui.theme.NightOwlPreview

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CATALOG,
        modifier = modifier
    ) {
        composable(Routes.CATALOG) {
            CatalogScreen(
                onBookClick = { bookId -> navController.navigate(Routes.bookDetail(bookId)) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(
            route = Routes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) {
            BookDetailScreen(
                onBack = { navController.popBackStack() },
                onFindSeat = { navController.navigate(Routes.ZONES) }
            )
        }

        composable(Routes.DISCOVER) {
            DiscoverScreen()
        }

        composable(Routes.ZONES) {
            SeatMapScreen(
                onReserve = { seatId -> navController.navigate(Routes.reserve(seatId)) }
            )
        }

        composable(
            route = Routes.RESERVE,
            arguments = listOf(
                navArgument("seatId") { type = NavType.IntType },
                navArgument("reservationId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            val isEditing = (entry.arguments?.getInt("reservationId") ?: -1) > 0
            ReserveSeatScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    if (isEditing) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(Routes.MY_RESERVATIONS) {
                            popUpTo(Routes.ZONES)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.MY_RESERVATIONS) {
            MyReservationsScreen(
                onEdit = { reservation ->
                    navController.navigate(Routes.editReservation(reservation.seatId, reservation.id))
                }
            )
        }

        composable(Routes.READING_LIST) {
            ReadingListScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@PreviewLightDark
@Composable
private fun AppNavHostPreview() {
    NightOwlPreview {
        AppNavHost(navController = rememberNavController())
    }
}