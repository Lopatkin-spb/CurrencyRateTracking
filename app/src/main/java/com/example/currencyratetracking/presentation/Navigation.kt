package com.example.currencyratetracking.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.currencyratetracking.currencies.presentation.CurrenciesScreen
import com.example.currencyratetracking.presentation.favorites.FavoritesScreen

@Composable
internal fun BottomNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Currencies.uniqueTag,
    ) {
        composable(route = Currencies.uniqueTag) {
            CurrenciesScreen()
        }
        composable(route = Favorites.uniqueTag) {
            FavoritesScreen()
        }
    }
}

internal fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route = route) {

        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = false
        }

        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true

        // Restore state when reselecting a previously selected item
        restoreState = false
    }
}

internal fun NavHostController.navigateSingleTopTo(route: String, arg: String) {
    this.navigateSingleTopTo("$route/$arg")
}