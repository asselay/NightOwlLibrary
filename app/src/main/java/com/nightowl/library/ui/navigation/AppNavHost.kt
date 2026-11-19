package com.nightowl.library.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nightowl.library.ui.screens.BookDetailScreen
import com.nightowl.library.ui.screens.CatalogScreen
import com.nightowl.library.ui.screens.MyReservationsScreen
import com.nightowl.library.ui.screens.ReserveSeatScreen
import com.nightowl.library.ui.screens.SeatMapScreen

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
                onBookClick = { bookId -> navController.navigate(Routes.bookDetail(bookId)) }
            )
        }

        composable(
            route = Routes.BOOK_DETAIL,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) {
            BookDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SEATS) {
            SeatMapScreen(
                onSeatClick = { seatId -> navController.navigate(Routes.reserve(seatId)) }
            )
        }

        composable(
            route = Routes.RESERVE,
            arguments = listOf(navArgument("seatId") { type = NavType.IntType })
        ) {
            ReserveSeatScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(Routes.RESERVATIONS) {
                        popUpTo(Routes.SEATS)
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.RESERVATIONS) {
            MyReservationsScreen()
        }
    }
}