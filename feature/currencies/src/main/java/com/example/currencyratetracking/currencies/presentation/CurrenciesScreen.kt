package com.example.currencyratetracking.currencies.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyratetracking.core.*
import com.example.currencyratetracking.core.presentation.MultiViewModelFactory
import com.example.currencyratetracking.currencies.R
import com.example.currencyratetracking.currencies.di.CurrenciesComponentProvider
import com.example.currencyratetracking.model.Sorting
import com.example.currencyratetracking.ui_theme.CurrencyRateTrackingTheme
import com.example.currencyratetracking.ui_theme.Default
import com.example.currencyratetracking.ui_theme.Secondary
import javax.inject.Inject


@Stable
class CurrenciesDaggerContainer {
    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory
}


@Composable
fun CurrenciesScreen(
    context: Context = LocalContext.current,
    container: CurrenciesDaggerContainer = remember {
        CurrenciesDaggerContainer().also { container ->
            (context as CurrenciesComponentProvider).provideCurrenciesComponent().inject(container)
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

    ScreenBoxComponent {

        ToolbarSelectComponent(
            title = R.string.title_currencies,
            uiState = uiState,
            onEvent = onEvent,
        )

        CardsListSection(
            modifier = Modifier.padding(start = 16.dp, top = 117.dp, end = 16.dp),
            list = uiState.listActualCurrencyRates,
            onFavoriteEvent = { data -> onEvent(CurrenciesUserEvent.OnChangeFavoriteState(data)) },
        )

        if (uiState.isFiltersLifecycle != null) {
            FiltersSection(modifier = modifier, uiState = uiState, onEvent = onEvent)
        }
    }
}


//TODO: add surface to toolbar
@Composable
private fun ToolbarSelectComponent(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    @StringRes title: Int,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    ToolbarComponent(title = title) {

        Row(
            modifier = Modifier.fillMaxWidth().height(68.dp)
                .background(MaterialTheme.colorScheme.surface)
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
                    .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.small,
                    )
                    .clickable(onClick = { onEvent(CurrenciesUserEvent.OnOpenFilters) }),
            ) {
                Icon(
                    modifier = Modifier.size(24.dp).align(Alignment.Center),
                    painter = painterResource(R.drawable.ic_filter_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

//todo: optimize code
@Composable
private fun CurrencyDropdownComponent(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    context: Context = LocalContext.current,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val width = remember { mutableStateOf(0f) }
    val iconId = if (expanded.value) {
        R.drawable.ic_arrow_up_24
    } else {
        R.drawable.ic_arrow_down_24
    }

    Row(
        modifier = modifier
            .fillMaxHeight()
            .onSizeChanged {
                val screenPixelsDensity = context.resources.displayMetrics.density
                val dp = it.width.toFloat() / screenPixelsDensity
                width.value = dp
            }
            .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.small),
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp).wrapContentHeight().weight(weight = 1f, fill = true)
                .align(Alignment.CenterVertically),
            text = uiState.showedBaseCurrency,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = { expanded.value = !expanded.value }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        DropdownMenu(
            modifier = Modifier
                .wrapContentHeight()
                .width(width.value.dp)
                .background(Default, shape = MaterialTheme.shapes.small)
                .border(width = 1.dp, color = Secondary, shape = MaterialTheme.shapes.small),
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
                        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
                        text = {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = nameCurrency,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.padding(end = 16.dp).size(24.dp),
                                painter = painterResource(iconId),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        },
                    )
                } else {
                    DropdownMenuItem(
                        modifier = Modifier.height(56.dp),
                        onClick = {
                            expanded.value = false
                            onEvent(CurrenciesUserEvent.OnChangeBaseCurrency(nameCurrency))
                        },
                        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
                        text = { Text(text = nameCurrency, style = MaterialTheme.typography.titleMedium) },
                    )
                }
            }
        }


    }
}


@Composable
private fun FiltersSection(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    ModalBottomSheetWithOutsideControl(
        state = uiState.isFiltersLifecycle,
        onResetState = { onEvent(CurrenciesUserEvent.OnResetFiltersState) },
        sheetContent = { SheetContent(modifier, uiState, onEvent) },
    )
}


@Composable
private fun SheetContent(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {
    ScreenColumnComponent {
        Box(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.systemBars)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        )

        ToolbarComponent(
            title = R.string.title_filters,
            leadingIcon = R.drawable.ic_arrow_back_24,
            onLeadingIcon = { onEvent(CurrenciesUserEvent.OnCloseFilters) },
        )

        SortingSection(
            modifier = Modifier.wrapContentHeight().fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            uiState = uiState,
            onEvent = onEvent,
        )

        Spacer(modifier = Modifier.weight(weight = 1f, fill = true))

        ButtonWithTextComponent(
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth().height(40.dp),
            text = R.string.action_apply,
            onClick = { onEvent(CurrenciesUserEvent.OnApplyFilters) },
        )

        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
    }
}


@Composable
private fun SortingSection(
    modifier: Modifier = Modifier,
    uiState: CurrenciesUiState,
    onEvent: (CurrenciesUserEvent) -> Unit,
) {

    Box(modifier = modifier) {
        Text(
            modifier = Modifier.wrapContentSize().align(Alignment.TopStart),
            text = stringResource(R.string.header_sort_by),
            style = MaterialTheme.typography.labelMedium,
        )
//todo: optimize
        Column(modifier = Modifier.padding(top = 32.dp).selectableGroup()) {
            SortingSelectComponent(
                selected = uiState.sorting == Sorting.CodeAZ,
                text = R.string.text_sorting_code_a_z,
                onClick = { onEvent(CurrenciesUserEvent.OnSortingSelect(Sorting.CodeAZ)) },
            )
            SortingSelectComponent(
                selected = uiState.sorting == Sorting.CodeZA,
                text = R.string.text_sorting_code_z_a,
                onClick = { onEvent(CurrenciesUserEvent.OnSortingSelect(Sorting.CodeZA)) },
            )
            SortingSelectComponent(
                selected = uiState.sorting == Sorting.QuoteAsc,
                text = R.string.text_sorting_quote_asc,
                onClick = { onEvent(CurrenciesUserEvent.OnSortingSelect(Sorting.QuoteAsc)) },
            )
            SortingSelectComponent(
                selected = uiState.sorting == Sorting.QuoteDesc,
                text = R.string.text_sorting_quote_desc,
                onClick = { onEvent(CurrenciesUserEvent.OnSortingSelect(Sorting.QuoteDesc)) },
            )
        }
    }
}

@Composable
private fun SortingSelectComponent(
    modifier: Modifier = Modifier,
    selected: Boolean,
    @StringRes text: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.height(48.dp).fillMaxWidth().selectable(
            selected = selected,
            onClick = onClick,
        ),
    ) {

        Text(
            modifier = Modifier.wrapContentHeight().weight(weight = 1f, fill = true).align(Alignment.CenterVertically),
            text = stringResource(text),
            style = MaterialTheme.typography.titleMedium,
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.secondary
            ),
        )
    }
}


//todo: bug preview - 2 status bars, bar must color
@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
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

    CurrencyRateTrackingTheme {
        Content(
            uiState = CurrenciesUiState(listActualCurrencyRates = listStub, isFiltersLifecycle = true),
            onEvent = {}
        )
    }
}