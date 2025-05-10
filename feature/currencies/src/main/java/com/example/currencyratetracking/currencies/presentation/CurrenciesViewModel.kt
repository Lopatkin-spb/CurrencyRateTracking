package com.example.currencyratetracking.currencies.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.Tag
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.presentation.AbstractLoggingViewModel
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.core.transformToList
import com.example.currencyratetracking.currencies.domain.*
import com.example.currencyratetracking.currencies.presentation.CurrenciesUiState.Empty.toLoading
import com.example.currencyratetracking.currencies.presentation.CurrenciesUiState.Empty.toSuccess
import com.example.currencyratetracking.model.Sorting
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlin.coroutines.cancellation.CancellationException


class CurrenciesViewModel @AssistedInject constructor(
    private val getListBaseCurrenciesUseCase: GetListBaseCurrenciesUseCase,
    private val getListActualCurrencyRatesByBaseCharCodeUseCase: GetListActualCurrencyRatesByBaseCharCodeUseCase,
    private val getUserSelectedBaseCurrencyUseCase: GetUserSelectedBaseCurrencyUseCase,
    private val dispatcher: BaseCoroutineDispatcher,
    private val logger: BaseLogger,
    private val setPairCurrenciesToFavoriteUseCase: Lazy<SetPairCurrenciesToFavoriteUseCase>,
    private val deletePairCurrenciesFromFavoriteByCharCodesUseCase: Lazy<DeletePairCurrenciesFromFavoriteByCharCodesUseCase>,
    private val setUserSelectedBaseCurrencyUseCase: Lazy<SetUserSelectedBaseCurrencyUseCase>,
    private val getListActualCurrencyRatesWithSortByBaseCharCodeUseCase: Lazy<GetListActualCurrencyRatesWithSortByBaseCharCodeUseCase>,
    private val tag: Tag,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : AbstractLoggingViewModel<CurrenciesUserEvent>(logger, tag,) {

    @AssistedFactory
    interface Factory : ViewModelAssistedSavedStateFactory<CurrenciesViewModel>

    companion object {
        private const val NAME_LOAD_LIST_BASE_CURRENCIES: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_LOAD_LIST_BASE_CURRENCIES"
        private const val NAME_SAVE_PAIR_TO_FAVORITE: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_SAVE_PAIR_TO_FAVORITE"
        private const val NAME_DELETE_PAIR_FROM_FAVORITE: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_DELETE_PAIR_FROM_FAVORITE"
        private const val NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES"
        private const val NAME_LOAD_BASE_CURRENCY: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_LOAD_BASE_CURRENCY"
        private const val NAME_SAVE_BASE_CURRENCY: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_SAVE_BASE_CURRENCY"
        private const val NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES_WITH_SORT: String =
            "com.example.currencyratetracking.currencies.presentation.NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES_WITH_SORT"
    }

    private val _uiState: MutableStateFlow<CurrenciesUiState> = MutableStateFlow(CurrenciesUiState.Empty)
    val uiState: StateFlow<CurrenciesUiState> get() = _uiState.asStateFlow()
    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, cause -> handle(cause, "$coroutineContext") }

    init {
        loadListBaseCurrencies()
    }


    override fun handle(new: CurrenciesUserEvent) {
        super.handle(new)

        when (new) {
            is CurrenciesUserEvent.OnScreenOpen -> selectLoading()
            is CurrenciesUserEvent.OnScreenClose -> {}
            is CurrenciesUserEvent.OnChangeBaseCurrency ->
                if (new.name != _uiState.value.showedBaseCurrency) saveBaseCurrency(new.name)

            is CurrenciesUserEvent.OnChangeFavoriteState -> selectActionToFavorite(new.currency.toActualCurrencyRateUi())
            is CurrenciesUserEvent.OnOpenFilters ->
                _uiState.update { state -> state.toSuccess().copy(isFiltersLifecycle = true) }

            is CurrenciesUserEvent.OnCloseFilters ->
                _uiState.update { state -> state.toSuccess().copy(isFiltersLifecycle = false) }

            is CurrenciesUserEvent.OnResetFiltersState ->
                _uiState.update { state -> state.toSuccess().copy(isFiltersLifecycle = null) }

            is CurrenciesUserEvent.OnApplyFilters ->
                _uiState.updateAndGet { state ->
                    state.toSuccess().copy(isFiltersLifecycle = false, isSortingApply = true)
                }.also { loadListActualCurrencyRatesWithSort(it.showedBaseCurrency, it.sorting) }

            is CurrenciesUserEvent.OnSortingSelect ->
                _uiState.update { state -> state.toSuccess().copy(sorting = new.select) }
        }
    }

    private fun selectLoading() {
        _uiState.value.also { state ->
            if (state.showedBaseCurrency.isEmpty()) {
                loadBaseCurrency()
            } else if (state.isSortingApply) {
                loadListActualCurrencyRatesWithSort(state.showedBaseCurrency, state.sorting)
            } else {
                loadListActualCurrencyRates(state.showedBaseCurrency)
            }
        }
    }

    private fun selectActionToFavorite(currency: ActualCurrencyRateUi) {
        _uiState.value.also { state ->
            if (currency.isFavorite) savePairToFavorite(currency, state.showedBaseCurrency)
            else deletePairFromFavorite(currency, state.showedBaseCurrency)
        }
    }

    private fun loadBaseCurrency() {
        getUserSelectedBaseCurrencyUseCase.execute()
            .onStart { _uiState.update { state -> state.toLoading() } }
            .onEach { currencyCharCode ->
                _uiState.update { state -> state.toLoading().copy(showedBaseCurrency = currencyCharCode) }
            }
            .onEach { currencyCharCode -> loadListActualCurrencyRates(currencyCharCode) }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_LOAD_BASE_CURRENCY),
                funLogName = NAME_FULL,
            )
    }

    private fun saveBaseCurrency(currency: String) {
        setUserSelectedBaseCurrencyUseCase.get().execute(currency)
            .onStart { _uiState.update { state -> state.toLoading() } }
            .onEach { isSuccess ->
                _uiState.updateAndGet { state ->
                    if (isSuccess) state.toLoading().copy(showedBaseCurrency = currency) else state
                }.also { state ->
                    if (isSuccess && state.isSortingApply) {
                        loadListActualCurrencyRatesWithSort(state.showedBaseCurrency, state.sorting)
                    } else if (isSuccess && !state.isSortingApply) {
                        loadListActualCurrencyRates(state.showedBaseCurrency)
                    }
                }
            }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_SAVE_BASE_CURRENCY),
                funLogName = NAME_FULL,
            )
    }

    //TODO: bug if list > screen then dropdownmenu unsize
    private fun loadListBaseCurrencies() {
        getListBaseCurrenciesUseCase.execute()
            .onStart { _uiState.update { state -> state.toLoading() } }
            .onEach { list -> _uiState.update { state -> state.toLoading().copy(listBaseCurrencies = list) } }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_LOAD_LIST_BASE_CURRENCIES),
                funLogName = NAME_FULL,
            )
    }

    private fun loadListActualCurrencyRates(name: String) {
        getListActualCurrencyRatesByBaseCharCodeUseCase.execute(name)
            .onStart { _uiState.update { state -> state.toLoading() } }
            .map { currency -> currency.toActualCurrencyRateUi() }
            .transformToList()
            .cancellable()
            .onEach { list -> _uiState.update { state -> state.toSuccess().copy(listActualCurrencyRates = list) } }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES),
                funLogName = NAME_FULL,
            )
    }

    private fun loadListActualCurrencyRatesWithSort(name: String, sorting: Sorting?) {
        getListActualCurrencyRatesWithSortByBaseCharCodeUseCase.get().execute(name, sorting)
            .onStart { _uiState.update { state -> state.toLoading() } }
            .map { currency -> currency.toActualCurrencyRateUi() }
            .transformToList()
            .cancellable()
            .onEach { list -> _uiState.update { state -> state.toSuccess().copy(listActualCurrencyRates = list) } }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler +
                        CoroutineName(NAME_LOAD_LIST_ACTUAL_CURRENCY_RATES_WITH_SORT),
                funLogName = NAME_FULL,
            )
    }

    private fun savePairToFavorite(currency: ActualCurrencyRateUi, baseCharCode: String) {
        setPairCurrenciesToFavoriteUseCase.get().execute(second = currency.text, base = baseCharCode)
            .map { isSaved -> currency.copy(isFavorite = isSaved) }
            .cancellable()
            .onEach { newCurrency -> updateListActualCurrencyRates(newCurrency) }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_SAVE_PAIR_TO_FAVORITE),
                funLogName = NAME_FULL,
            )
    }

    private fun deletePairFromFavorite(secondCurrency: ActualCurrencyRateUi, baseCharCode: String) {
        deletePairCurrenciesFromFavoriteByCharCodesUseCase.get()
            .execute(base = baseCharCode, second = secondCurrency.text)
            .map { isSaved -> secondCurrency.copy(isFavorite = !isSaved) }
            .cancellable()
            .onEach { newCurrency -> updateListActualCurrencyRates(newCurrency) }
            .onCompletion { if (it is CancellationException) _uiState.update { CurrenciesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_DELETE_PAIR_FROM_FAVORITE),
                funLogName = NAME_FULL,
            )
    }

    private fun updateListActualCurrencyRates(new: ActualCurrencyRateUi) {
        _uiState.update { currentState ->
            val newList = arrayListOf<ActualCurrencyRateUi>()

            currentState.listActualCurrencyRates.forEach { currentItem ->
                if (currentItem.id == new.id) {
                    newList.add(new)
                } else {
                    newList.add(currentItem)
                }
            }
            currentState.toSuccess().copy(listActualCurrencyRates = newList)
        }
    }


    override fun handle(cause: Throwable, details: String) {
        super.handle(cause, details)
        _uiState.update { CurrenciesUiState.Error(text = cause.message ?: "Unspecified error") }
        //& next - errors send to analytic
    }

}