package com.example.currencyratetracking.core.presentation

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.currencyratetracking.model.CurrencyUi
import com.example.currencyratetracking.ui_theme.R
import kotlinx.coroutines.launch


@Composable
fun ScreenBoxComponent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun ScreenColumnComponent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        content()
    }
}


@Composable
fun ToolbarComponent(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    @DrawableRes leadingIcon: Int? = null,
    onLeadingIcon: (() -> Unit)? = null,
    content: @Composable () -> Unit = {},
) {
    val startPaddingTitle = remember { mutableStateOf(16) }
    if (onLeadingIcon != null && leadingIcon != null) startPaddingTitle.value = 56
    else startPaddingTitle.value = 16

    Column(modifier = modifier.wrapContentSize()) {
        Box(
            modifier = Modifier.height(48.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surface),
        ) {
            if (onLeadingIcon != null && leadingIcon != null) {
                Box(modifier = Modifier.padding(start = 4.dp).size(48.dp).clickable(onClick = onLeadingIcon)) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(leadingIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(start = startPaddingTitle.value.dp)
                    .wrapContentHeight()
                    .width(180.dp)
                    .align(Alignment.CenterStart),
                text = stringResource(title),
                style = MaterialTheme.typography.displayLarge,
            )
        }

        content()

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline))
    }
}


@Composable
fun CardsListSection(
    modifier: Modifier = Modifier,
    list: List<CurrencyUi>,
    onFavoriteEvent: (CurrencyUi) -> Unit,
) {

    LazyColumn(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
    ) {
        items(
            items = list,
            key = { item -> item.id },
        ) { item ->
            CardItem(
                data = item,
                onFavoriteEvent = onFavoriteEvent,
            )
        }
    }
}


@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    data: CurrencyUi,
    onFavoriteEvent: (CurrencyUi) -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.medium)
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            modifier = Modifier.wrapContentHeight()
                .weight(weight = 1f, fill = true)
                .align(Alignment.CenterVertically),
            text = data.text,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
        )

        Row(modifier = Modifier.wrapContentHeight().width(116.dp).align(Alignment.CenterVertically)) {
            Text(
                modifier = Modifier.wrapContentHeight()
                    .weight(weight = 1f, fill = true)
                    .align(Alignment.CenterVertically),
                text = data.quotation,
                style = MaterialTheme.typography.bodyLarge,
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
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites_off_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}


//todo: bug - must save open state to rotate
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetWithOutsideControl(
    sheetContent: @Composable (ColumnScope.() -> Unit),
    state: Boolean?,
    onResetState: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onResetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        shape = MaterialTheme.shapes.extraSmall,
    ) {
        sheetContent()
    }

    // Outside management
    state?.let { isShow ->
        scope.launch {
            if (isShow && !sheetState.isVisible) {
                sheetState.show()
            }
            if (!isShow && sheetState.isVisible) {
                sheetState.hide()
                onResetState()
            }
        }
    }

    // Listener for swipe close
    if (sheetState.isVisible) {
        DisposableEffect(Unit) {
            onDispose {
                onResetState()
            }
        }
    }

    // Listener for system back button
    BackHandler(sheetState.isVisible) {
        scope.launch {
            sheetState.hide()
            onResetState()
        }
    }

}


@Composable
fun ButtonWithTextComponent(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}