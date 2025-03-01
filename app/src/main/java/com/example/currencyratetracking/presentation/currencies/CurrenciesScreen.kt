package com.example.currencyratetracking.presentation.currencies

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyratetracking.presentation.MainActivity
import com.example.currencyratetracking.presentation.OnLifecycleScreen
import com.example.currencyratetracking.presentation.ViewModelFactory
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
    val text by viewModel.text.observeAsState()

    OnLifecycleScreen(
        onStart = { viewModel.handle(CurrenciesUserEvent.OnScreenOpen) },
        onStop = { viewModel.handle(CurrenciesUserEvent.OnScreenClose) },
    )

    Screen(text)
}

@Composable
private fun Screen(
    text: String?,
    modifier: Modifier = Modifier
) {
    text?.let { text ->
        Box(modifier.fillMaxSize()) {
            Text(
                text = text,
                modifier = modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Screen(text = "CurrenciesFragment")
        }
    }
}