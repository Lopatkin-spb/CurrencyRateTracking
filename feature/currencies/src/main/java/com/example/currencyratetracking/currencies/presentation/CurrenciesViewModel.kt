package com.example.currencyratetracking.currencies.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.currencies.ModuleTag.TAG_LOG
import com.example.currencyratetracking.currencies.toCurrency
import com.example.currencyratetracking.currencies.toCurrencyPair
import com.example.currencyratetracking.currencies.toFavoriteCurrencyPairDbo
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.model.CurrencyUi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CurrenciesViewModel(
    private val api: RatesApi,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
    private val logger: BaseLogger,
) : AbstractViewModel() {

    private val _uiState = MutableLiveData(CurrenciesUiState())
    val uiState: LiveData<CurrenciesUiState> = _uiState

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")

        loadListBaseCurrencies()
        _uiState.value?.showedBaseCurrency?.let { loadListActualCurrencyRates(it) }
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
                setShowedBaseCurrency(new.name)
                loadListActualCurrencyRates(new.name)
            }

            is CurrenciesUserEvent.OnChangeFavoriteState -> {
                logger.i(TAG_LOG, "$NAME_FULL OnChangeFavoriteState")
                updateListActualCurrencyRates(new.currency)
                if (new.currency.isFavorite) savePairToFavorite(new.currency)
                else deletePairFromFavorite(new.currency)
            }
        }
    }

    //TODO: bug if list > screen then dropdownmenu unsize
    private fun loadListBaseCurrencies() {
        logger.d(TAG_LOG, "$NAME_FULL started")
        val listStub = mutableListOf<String>()
        val enumEntries = CurrencyInfo.entries
        for (index in 0 until enumEntries.size - 7) {
            val name = enumEntries[index].toString()

            if (index == 0) setShowedBaseCurrency(name)
            listStub.add(name)
        }
        _uiState.value = _uiState.value?.copy(listBaseCurrencies = listStub)
    }

    private fun setShowedBaseCurrency(name: String) {
        _uiState.value = _uiState.value?.copy(showedBaseCurrency = name)
    }

    private fun loadListActualCurrencyRates(name: String) {
        logger.d(TAG_LOG, "$NAME_FULL started")
        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_FULL launch")

                    val result = api.getRates(name)
                    logger.v(TAG_LOG, "$NAME_FULL ended result = $result")

                    val list = result.rates?.getListRatesDto()?.asSequence()
                        ?.map { dto -> dto.toCurrency() }
                        ?.filterNot { model -> model.quotation == 0.0 }
                        ?.filterNot { model -> model.charCode.name == result.base }
                        ?.map { model -> model.toActualCurrencyRateUi() }
                        ?.toList() ?: emptyList()

                    _uiState.value = _uiState.value?.copy(listActualCurrencyRates = list)

                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_FULL error $t", t)
            }
        }
    }


    private fun savePairToFavorite(currency: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_FULL started")
        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_FULL launch")
                    _uiState.value?.let { state ->

                        val model = currency.toCurrencyPair(state.showedBaseCurrency)
                        val dbo = model.toFavoriteCurrencyPairDbo()
                        favoriteCurrencyPairApi.checkAndInsertUniquePair(dbo)
                    }

                    logger.v(TAG_LOG, "$NAME_FULL ended")
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_FULL error $t", t)
            }
        }
    }

    private fun updateListActualCurrencyRates(new: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_FULL started")

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


    private fun deletePairFromFavorite(currency: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_FULL started $currency")

        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_FULL launch")
                    _uiState.value?.let { state ->

                        val model = currency.toCurrencyPair(state.showedBaseCurrency)
                        val dbo = model.toFavoriteCurrencyPairDbo()
                        favoriteCurrencyPairApi.deleteAll(dbo)
                    }
                    logger.v(TAG_LOG, "$NAME_FULL ended")
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_FULL error $t", t)
            }
        }
    }


    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onCleared()
    }
}