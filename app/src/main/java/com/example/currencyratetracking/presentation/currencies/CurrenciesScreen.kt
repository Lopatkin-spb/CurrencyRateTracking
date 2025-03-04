package com.example.currencyratetracking.presentation.currencies

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.currencyratetracking.presentation.*
import com.example.currencyratetracking.presentation.theme.CurrencyRateTrackingTheme
import javax.inject.Inject


@Stable
class CurrenciesDaggerContainer {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}


@Composable
internal fun CurrenciesScreen(
    context: Context = LocalContext.current,
    container: CurrenciesDaggerContainer = remember {
        CurrenciesDaggerContainer().also { container ->
            (context as MainActivity).getMainComponent().inject(container)
        }
    },
    viewModel: CurrenciesViewModel = viewModel(factory = container.viewModelFactory),
) {
    val uiState by viewModel.uiState.observeAsState()

    OnLifecycleScreen(
        onStart = { viewModel.handle(CurrenciesUserEvent.OnScreenOpen) },
        onStop = { viewModel.handle(CurrenciesUserEvent.OnScreenClose) },
    )

    uiState?.let { state ->
        Content(uiState = state, onEvent = {})
    }
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: () -> Unit,
) {

    ScreenComponent {

        ToolbarSelectComponent(title = R.string.title_currencies)

        CurrenciesListSection(
            modifier = Modifier.padding(start = 16.dp, top = 133.dp, end = 16.dp),
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}


@Composable
private fun CurrenciesListSection(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: () -> Unit,
) {

    LazyColumn(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = uiState.listActual,
            key = { currency -> currency.id },
        ) { favoritePair ->
            CurrencyItem(data = favoritePair)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
        // A surface container using the 'background' color from the theme
//        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        val listStub = mutableListOf<ActualCurrencyPair>()
        for (index in 1L..5) {
            val item = ActualCurrencyPair(
                id = index,
                name = "SDDF",
                quotation = 3.932455,
            )
            listStub.add(item)
        }

        Content(
            uiState = CurrenciesUiState(listActual = listStub),
            onEvent = {}
        )
//        }
    }
}