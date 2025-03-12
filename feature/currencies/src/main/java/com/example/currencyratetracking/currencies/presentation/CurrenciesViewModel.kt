package com.example.currencyratetracking.currencies.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.*
import com.example.currencyratetracking.currencies.ModuleTag.TAG_LOG
import com.example.currencyratetracking.currencies.domain.*
import com.example.currencyratetracking.model.CurrencyUi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class CurrenciesViewModel(
    private val getListBaseCurrenciesUseCase: GetListBaseCurrenciesUseCase,
    private val setPairCurrenciesToFavoriteUseCase: SetPairCurrenciesToFavoriteUseCase,
    private val deletePairCurrenciesFromFavoriteByCharCodesUseCase: DeletePairCurrenciesFromFavoriteByCharCodesUseCase,
    private val getListActualCurrencyRatesByBaseCharCodeUseCase: GetListActualCurrencyRatesByBaseCharCodeUseCase,
    private val getUserSelectedBaseCurrencyUseCase: GetUserSelectedBaseCurrencyUseCase,
    private val setUserSelectedBaseCurrencyUseCase: SetUserSelectedBaseCurrencyUseCase,
    private val dispatcher: BaseCoroutineDispatcher,
    private val logger: BaseLogger,
) : AbstractViewModel() {

    companion object {
        private const val LOAD_LIST_BASE_CURRENCIES_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.LOAD_LIST_BASE_CURRENCIES_KEY"
        private const val SAVE_PAIR_TO_FAVORITE_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.SAVE_PAIR_TO_FAVORITE_KEY"
        private const val DELETE_PAIR_FROM_FAVORITE_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.DELETE_PAIR_FROM_FAVORITE_KEY"
        private const val LOAD_LIST_ACTUAL_CURRENCY_RATES_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.LOAD_LIST_ACTUAL_CURRENCY_RATES_KEY"
        private const val LOAD_BASE_CURRENCY_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.LOAD_BASE_CURRENCY_KEY"
        private const val SAVE_BASE_CURRENCY_KEY: String =
            "com.example.currencyratetracking.currencies.presentation.SAVE_BASE_CURRENCY_KEY"
    }

    //TODO: add loading

    private val _uiState = MutableLiveData(CurrenciesUiState())
    val uiState: LiveData<CurrenciesUiState> = _uiState
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, cause ->
        logger.e(TAG_LOG, "$NAME_CLASS CoroutineExceptionHandler: $coroutineContext", cause)
    }

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")

        loadListBaseCurrencies()
        loadBaseCurrency()
    }


    fun handle(new: CurrenciesUserEvent) {
        when (new) {
            is CurrenciesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_FULL OnScreenOpen")
            }

            is CurrenciesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_FULL OnScreenClose")
            }

            is CurrenciesUserEvent.OnChangeBaseCurrency -> {
                logger.i(TAG_LOG, "$NAME_FULL OnChangeBaseCurrency")
                saveBaseCurrency(new.name)
            }

            is CurrenciesUserEvent.OnChangeFavoriteState -> {
                logger.i(TAG_LOG, "$NAME_FULL OnChangeFavoriteState")
                if (new.currency.isFavorite) savePairToFavorite(new.currency)
                else deletePairFromFavorite(new.currency)
            }
        }
    }


    private fun loadBaseCurrency() {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(LOAD_BASE_CURRENCY_KEY)) {
            getUserSelectedBaseCurrencyUseCase.execute()
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { result ->
                    _uiState.value = _uiState.value?.copy(showedBaseCurrency = result)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                    loadListActualCurrencyRates(result)
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    private fun saveBaseCurrency(currency: String) {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(SAVE_BASE_CURRENCY_KEY)) {
            setUserSelectedBaseCurrencyUseCase.execute(currency)
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { result ->
                    if (result) _uiState.value = _uiState.value?.copy(showedBaseCurrency = currency)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                    if (result) loadListActualCurrencyRates(currency)
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    //TODO: bug if list > screen then dropdownmenu unsize
    private fun loadListBaseCurrencies() {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(LOAD_LIST_BASE_CURRENCIES_KEY)) {
            getListBaseCurrenciesUseCase.execute()
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { list ->
                    _uiState.value = _uiState.value?.copy(listBaseCurrencies = list)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    private fun loadListActualCurrencyRates(name: String) {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(LOAD_LIST_ACTUAL_CURRENCY_RATES_KEY)) {
            getListActualCurrencyRatesByBaseCharCodeUseCase.execute(name)
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .map { model -> model.toActualCurrencyRateUi() }
                .transformToList()
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { list ->
                    _uiState.value = _uiState.value?.copy(listActualCurrencyRates = list)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    private fun savePairToFavorite(currency: CurrencyUi) {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(SAVE_PAIR_TO_FAVORITE_KEY)) {
            setPairCurrenciesToFavoriteUseCase.execute(
                second = currency.text,
                base = _uiState.value?.showedBaseCurrency
            )
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .map { result ->
                    CurrencyUi(
                        id = currency.id,
                        text = currency.text,
                        quotation = currency.quotation,
                        isFavorite = result,
                    )
                }
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { model ->
                    updateListActualCurrencyRates(model)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    //TODO: optimize in future
    private fun updateListActualCurrencyRates(new: CurrencyUi) {
        logger.v(TAG_LOG, "$NAME_FULL started")

        val newList = arrayListOf<ActualCurrencyRateUi>()
        _uiState.value?.listActualCurrencyRates?.let {
            it.forEach {
                if (it.id == new.id) {
                    newList.add(
                        ActualCurrencyRateUi(
                            id = new.id,
                            text = new.text,
                            quotation = new.quotation,
                            isFavorite = new.isFavorite,
                        )
                    )
                } else {
                    newList.add(it)
                }
            }
        }
        _uiState.value = _uiState.value?.copy(listActualCurrencyRates = newList)
    }


    //TODO: if on Favorite screen deleted, then this not deleted
    private fun deletePairFromFavorite(currency: CurrencyUi) {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(DELETE_PAIR_FROM_FAVORITE_KEY)) {
            deletePairCurrenciesFromFavoriteByCharCodesUseCase.execute(
                base = _uiState.value?.showedBaseCurrency,
                second = currency.text,
            )
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .map { result ->
                    CurrencyUi(
                        id = currency.id,
                        text = currency.text,
                        quotation = currency.quotation,
                        isFavorite = !result,
                    )
                }
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { model ->
                    updateListActualCurrencyRates(model)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }


    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onCleared()
    }
}