package com.example.currencyratetracking.favorites.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyratetracking.core.presentation.*
import com.example.currencyratetracking.favorites.R
import com.example.currencyratetracking.favorites.di.FavoritesComponentProvider
import com.example.currencyratetracking.ui_theme.CurrencyRateTrackingTheme


@Composable
fun FavoritesScreen(
    context: Context = LocalContext.current,
    viewModel: FavoritesViewModel = daggerAssistedViewModel { stateHandle ->
        (context as FavoritesComponentProvider).provideFavoritesComponent().getFavoritesViewModel().create(stateHandle)
    },
) {
    val uiState by viewModel.uiState.collectAsState()

    OnLifecycleScreen(
        onStart = { viewModel.handle(FavoritesUiEvent.OnScreenOpen) },
        onStop = { viewModel.handle(FavoritesUiEvent.OnScreenClose) },
    )

    Content(
        uiState = uiState,
        onEvent = { event -> viewModel.handle(event) },
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: FavoritesUiState,
    onEvent: (FavoritesUiEvent) -> Unit,
) {

    ScreenBoxComponent {

        ToolbarComponent(title = R.string.title_favorites)

        when (uiState) {
            is FavoritesUiState.Loading -> IndeterminateCircularIndicator()
            is FavoritesUiState.Error -> {}
            is FavoritesUiState.Empty -> {}
            is FavoritesUiState.Success ->
                CardsListSection(
                    modifier = Modifier.padding(start = 16.dp, top = 49.dp, end = 16.dp),
                    list = uiState.listFavorites,
                    onFavoriteEvent = { data -> onEvent(FavoritesUiEvent.OnChangeFavoriteState(data)) },
                )
        }
    }
}


@Composable
private fun IndeterminateCircularIndicator(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier.padding(top = 100.dp).fillMaxWidth().wrapContentHeight()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(40.dp).align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
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
//    val uiStateStub = FavoritesUiState2.Success(listFavorites = listStub)
    val uiStateStub = FavoritesUiState.Loading

    CurrencyRateTrackingTheme {
        Content(
            onEvent = {},
            uiState = uiStateStub,
        )
    }
}