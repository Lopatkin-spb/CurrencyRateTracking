package com.example.currencyratetracking.favorites.presentation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyratetracking.core.CardsListSection
import com.example.currencyratetracking.core.OnLifecycleScreen
import com.example.currencyratetracking.core.ScreenBoxComponent
import com.example.currencyratetracking.core.ToolbarComponent
import com.example.currencyratetracking.core.presentation.MultiViewModelFactory
import com.example.currencyratetracking.favorites.R
import com.example.currencyratetracking.favorites.di.FavoritesComponentProvider
import com.example.currencyratetracking.ui_theme.CurrencyRateTrackingTheme
import javax.inject.Inject


@Stable
class FavoritesDaggerContainer {
    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory
}

@Composable
fun FavoritesScreen(
    context: Context = LocalContext.current,
    container: FavoritesDaggerContainer = remember {
        FavoritesDaggerContainer().also { container ->
            (context as FavoritesComponentProvider).provideFavoritesComponent().inject(container)
        }
    },
    viewModel: FavoritesViewModel = viewModel(factory = container.viewModelFactory),
) {
    val uiState by viewModel.uiState.observeAsState()

    OnLifecycleScreen(
        onStart = { viewModel.handle(FavoritesUserEvent.OnScreenOpen) },
        onStop = { viewModel.handle(FavoritesUserEvent.OnScreenClose) },
    )

    uiState?.let { state ->
        Content(
            uiState = state,
            onEvent = { event -> viewModel.handle(event) },
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState,
    onEvent: (FavoritesUserEvent) -> Unit,
) {

    ScreenBoxComponent {

        ToolbarComponent(title = R.string.title_favorites)

        CardsListSection(
            modifier = Modifier.padding(start = 16.dp, top = 49.dp, end = 16.dp),
            list = uiState.listFavorites,
            onFavoriteEvent = { data -> onEvent(FavoritesUserEvent.OnChangeFavoriteState(data)) },
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    val listStub = mutableListOf<FavoritePairCurrenciesRateUi>()
    for (index in 1L..5) {
        val item = FavoritePairCurrenciesRateUi(
            id = index,
            text = "SDDF/JHY",
            quotation = "3.932455",
            isFavorite = true,
        )
        listStub.add(item)
    }

    CurrencyRateTrackingTheme {
        Content(
            onEvent = {},
            uiState = FavoritesUiState(listFavorites = listStub),
        )
    }
}