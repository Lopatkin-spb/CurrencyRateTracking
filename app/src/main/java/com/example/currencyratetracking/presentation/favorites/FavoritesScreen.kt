package com.example.currencyratetracking.presentation.favorites

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
import com.example.currencyratetracking.R
import com.example.currencyratetracking.core.CurrenciesListSection
import com.example.currencyratetracking.core.OnLifecycleScreen
import com.example.currencyratetracking.core.ScreenComponent
import com.example.currencyratetracking.core.ToolbarComponent
import com.example.currencyratetracking.presentation.*
import com.example.currencyratetracking.ui_theme.CurrencyRateTrackingTheme
import javax.inject.Inject


@Stable
class FavoritesDaggerContainer {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}

@Composable
internal fun FavoritesScreen(
    context: Context = LocalContext.current,
    container: FavoritesDaggerContainer = remember {
        FavoritesDaggerContainer().also { container ->
            (context as MainActivity).getMainComponent().inject(container)
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

    ScreenComponent {

        ToolbarComponent(title = R.string.title_favorites)

        CurrenciesListSection(
            modifier = Modifier.padding(start = 16.dp, top = 65.dp, end = 16.dp),
            list = uiState.listFavorites,
            onFavoriteEvent = { data -> onEvent(FavoritesUserEvent.OnChangeFavoriteState(data)) },
        )
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
//         A surface container using the 'background' color from the theme
//        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        val listStub = mutableListOf<FavoriteCurrencyRate>()
        for (index in 1L..5) {
            val item = FavoriteCurrencyRate(
                id = index,
                text = "SDDF/JHY",
                quotation = "3.932455",
                isFavorite = true,
            )
            listStub.add(item)
        }

        Content(
            onEvent = {},
            uiState = FavoritesUiState(listFavorites = listStub),
        )
    }
}