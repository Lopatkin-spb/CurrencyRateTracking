package com.example.currencyratetracking.presentation.currencies

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyratetracking.R
import com.example.currencyratetracking.presentation.*
import com.example.currencyratetracking.presentation.theme.*
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
        Content(
            uiState = state,
            onEvent = { event -> viewModel.handle(event) }
        )
    }
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {

    ScreenComponent {

        ToolbarSelectComponent(
            title = R.string.title_currencies,
            uiState = uiState,
            onEvent = onEvent,
        )

        CurrenciesListSection(
            modifier = Modifier.padding(start = 16.dp, top = 133.dp, end = 16.dp),
            list = uiState.listActualCurrencyRates,
            onFavoriteEvent = { data -> onEvent(CurrenciesUserEvent.OnChangeFavoriteState(data)) },
        )
    }
}


//TODO: bug add surface to toolbar
@Composable
private fun ToolbarSelectComponent(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    @StringRes title: Int,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    ToolbarComponent(title = title) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(Header)
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 12.dp),
        ) {

            CurrencyDropdownComponent(
                modifier = Modifier.weight(weight = 1f, fill = true),
                uiState = uiState,
                onEvent = onEvent,
            )

            Box(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(48.dp)
                    .background(Default, shape = MaterialTheme.shapes.medium)
                    .border(width = 1.dp, color = Secondary, shape = MaterialTheme.shapes.medium),
            ) {
                Icon(
                    modifier = Modifier.size(24.dp).align(Alignment.Center),
                    painter = painterResource(R.drawable.ic_filter_24),
                    contentDescription = null,
                    tint = Primary,
                )
            }
        }
    }
}


@Composable
private fun CurrencyDropdownComponent(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    context: Context = LocalContext.current,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val width = remember { mutableStateOf(0f) }

    Row(
        modifier = modifier
            .fillMaxHeight()
            .onSizeChanged {
                val screenPixelsDensity = context.resources.displayMetrics.density
                val dp = it.width.toFloat() / screenPixelsDensity
                width.value = dp
            }
            .background(Default, shape = MaterialTheme.shapes.medium)
            .border(width = 1.dp, color = Secondary, shape = MaterialTheme.shapes.medium),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .wrapContentHeight()
                .weight(weight = 1f, fill = true)
                .align(Alignment.CenterVertically),
            text = uiState.showedBaseCurrency,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle2,
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { expanded.value = !expanded.value }) {
            if (expanded.value) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_arrow_up_24),
                    contentDescription = null,
                    tint = Primary,
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_arrow_down_24),
                    contentDescription = null,
                    tint = Primary,
                )
            }
        }

        DropdownMenu(
            modifier = Modifier
                .wrapContentHeight()
                .width(width.value.dp)
                .background(Default, shape = MaterialTheme.shapes.medium)
                .border(width = 1.dp, color = Secondary, shape = MaterialTheme.shapes.medium),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            offset = DpOffset(0.dp, (-48).dp)
        ) {
            uiState.listBaseCurrencies.forEachIndexed { index, nameCurrency ->
                if (index == 0) {
                    DropdownMenuItem(
                        modifier = Modifier.height(34.dp),
                        onClick = {
                            expanded.value = false
                            onEvent(CurrenciesUserEvent.OnChangeBaseCurrency(nameCurrency))
                        },
                        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = nameCurrency,
                            style = MaterialTheme.typography.subtitle2,
                        )
                        Icon(
                            modifier = Modifier.padding(end = 16.dp).size(24.dp),
                            painter = painterResource(R.drawable.ic_arrow_up_24),
                            contentDescription = null,
                            tint = Primary,
                        )
                    }
                } else {
                    DropdownMenuItem(
                        modifier = Modifier.height(56.dp),
                        onClick = {
                            expanded.value = false
                            onEvent(CurrenciesUserEvent.OnChangeBaseCurrency(nameCurrency))
                        },
                        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                    ) {
                        Text(text = nameCurrency, style = MaterialTheme.typography.subtitle2)
                    }
                }
            }
        }


    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
        // A surface container using the 'background' color from the theme
//        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        val listStub = mutableListOf<ActualCurrencyRateUi>()
        for (index in 1L..5) {
            val item = ActualCurrencyRateUi(
                id = index,
                text = "TGR",
                quotation = "3.932455",
                isFavorite = false,
            )
            listStub.add(item)
        }

        Content(
            uiState = CurrenciesUiState(listActualCurrencyRates = listStub),
            onEvent = {}
        )
//        }
    }
}