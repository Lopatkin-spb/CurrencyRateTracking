package com.example.currencyratetracking.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.currencyratetracking.R
import com.example.currencyratetracking.presentation.theme.*


/**
 * Listener for composable screen lifecycle. Listen single event avoid recompositions.
 * For analytics, logs, and other events.
 */
@Composable
internal fun OnLifecycleScreen(
    onStart: () -> Unit = {},
    onStop: () -> Unit = {},
) {
    OnLifecycleComposable(
        onStart = onStart,
        onStop = onStop,
    )
}

@Composable
private fun OnLifecycleComposable(
    onStart: () -> Unit,
    onStop: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    // Safely update the current lambdas when a new one is provided
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {

        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            } else if (event == Lifecycle.Event.ON_STOP) {
                currentOnStop()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


@Composable
fun ScreenComponent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
    }
}


@Composable
fun ToolbarComponent(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    content: @Composable () -> Unit = {},
) {

    Column(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Header),
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .wrapContentHeight()
                    .width(180.dp)
                    .align(Alignment.CenterStart),
                text = stringResource(title),
                style = MaterialTheme.typography.h1,
            )
        }
        content()
        Box(
            modifier = Modifier.fillMaxWidth().height(1.dp).background(Outline),
        )
    }
}

//TODO: bug correct list must moved to under toolbar
@Composable
fun CurrenciesListSection(
    modifier: Modifier = Modifier,
    list: List<CurrencyUi>,
    onFavoriteEvent: (CurrencyUi) -> Unit,
) {

    LazyColumn(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = list,
            key = { item -> item.id },
        ) { item ->
            CurrencyItem(
                data = item,
                onFavoriteEvent = onFavoriteEvent,
            )
        }
    }
}


@Composable
fun CurrencyItem(
    modifier: Modifier = Modifier,
    data: CurrencyUi,
    onFavoriteEvent: (CurrencyUi) -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .background(color = Card, shape = MaterialTheme.shapes.large)
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(weight = 1f, fill = true)
                .align(Alignment.CenterVertically),
            text = data.text,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle2,
        )
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .width(116.dp)
                .align(Alignment.CenterVertically),
        ) {
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(weight = 1f, fill = true)
                    .align(Alignment.CenterVertically),
                text = data.quotation,
                style = MaterialTheme.typography.subtitle1,
            )
            IconToggleButton(
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                checked = data.isFavorite,
                onCheckedChange = {
                    val dataNew = CurrencyUi(
                        id = data.id,
                        text = data.text,
                        quotation = data.quotation,
                        isFavorite = it,
                    )
                    onFavoriteEvent(dataNew)
                },
            ) {
                if (data.isFavorite) {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites_on_24),
                        contentDescription = null,
                        tint = Yellow,
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites_off_24),
                        contentDescription = null,
                        tint = Secondary,
                    )
                }
            }
        }
    }
}