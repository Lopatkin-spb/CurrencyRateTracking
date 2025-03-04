package com.example.currencyratetracking.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
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


//TODO: rename, update
@Composable
fun ToolbarSelectComponent(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
) {
    val sizeParent = remember { mutableStateOf(IntSize.Zero) }

    ToolbarComponent(title = title) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .background(Header)
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 12.dp),
        ) {

            CurrencyDropdownComponent(modifier = Modifier.weight(weight = 1f, fill = true))

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
fun CurrencyDropdownComponent(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
) {
    val expanded = remember { mutableStateOf(false) }
    val menuItemData = listOf("ASD", "NHB", "TYU")
    val actualCurrencyName = remember { mutableStateOf("ABC") }
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
            text = actualCurrencyName.value,
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
                .width(width.value.dp)
                .background(Default, shape = MaterialTheme.shapes.medium)
                .border(width = 1.dp, color = Secondary, shape = MaterialTheme.shapes.medium),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            offset = DpOffset(0.dp, (-48).dp)
        ) {
            menuItemData.forEachIndexed { index, nameCurrency ->
                if (index == 0) {
                    DropdownMenuItem(
                        modifier = Modifier.height(34.dp),
                        onClick = {
                            expanded.value = false
                            actualCurrencyName.value = nameCurrency
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
                            actualCurrencyName.value = nameCurrency
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


open class Currency(
    open val id: Long,
    open val name: String,
    open val quotation: Double,
)


@Composable
fun CurrencyItem(
    modifier: Modifier = Modifier,
    data: Currency,
    onEvent: () -> Unit = {},
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
            text = data.name,
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
                text = "${data.quotation}",
                style = MaterialTheme.typography.subtitle1,
            )
            Icon(
                modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
                painter = painterResource(R.drawable.ic_favorites_on_24),
                contentDescription = null,
                tint = Yellow,
            )
        }
    }
}