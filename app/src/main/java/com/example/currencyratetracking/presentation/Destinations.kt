package com.example.currencyratetracking.presentation

import com.example.currencyratetracking.R


internal interface AppDestination {
    val uniqueTag: String
    val titleId: Int
}

internal interface BottomNavDestination : AppDestination {
    override val uniqueTag: String
    override val titleId: Int
    val iconId: Int
}

internal val bottomNavDestinations = listOf(
    Currencies,
    Favorites,
)

/**
 * Bottom navigation menu destinations
 */
internal object Currencies : BottomNavDestination {
    override val uniqueTag: String = "Currencies"
    override val titleId: Int = R.string.title_currencies
    override val iconId: Int = R.drawable.ic_baseline_credit_card_24_test
}

internal object Favorites : BottomNavDestination {
    override val uniqueTag: String = "Favorites"
    override val titleId: Int = R.string.title_favorites
    override val iconId: Int = R.drawable.ic_baseline_star_24_test
}

/**
 * Other destinations
 */
//internal data object TicketList : AppDestination {
//    override val uniqueTag: String = "TicketList"
//    override val titleId: Int = R.string.title_ticket_list
//    const val SEARCH_PLACES_ARG = "com.example.testtask_ticketssearch.SEARCH_PLACES_ARG"
//    val uniqueTagWithArgs: String = "$uniqueTag/{$SEARCH_PLACES_ARG}"
//    val arguments = listOf(
//        navArgument(SEARCH_PLACES_ARG) { type = NavType.StringType },
//    )
//}

