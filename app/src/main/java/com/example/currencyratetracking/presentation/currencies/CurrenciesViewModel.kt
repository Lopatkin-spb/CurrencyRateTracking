package com.example.currencyratetracking.presentation.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.presentation.CurrencyUi
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import com.example.currencyratetracking.presentation.toCurrency
import com.example.currencyratetracking.presentation.toCurrencyPair
import com.example.currencyratetracking.presentation.toFavoriteCurrencyPairDbo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


internal class CurrenciesViewModel(
    private val api: RatesApi,
    private val favoriteCurrencyPairApi: FavoriteCurrencyPairApi,
    private val logger: BaseLogger,
) : AbstractViewModel() {

    private val _uiState = MutableLiveData(CurrenciesUiState())
    val uiState: LiveData<CurrenciesUiState> = _uiState

    init {
        logger.d(TAG_LOG, "$NAME_CLASS init(): started")

        loadListBaseCurrencies()
        _uiState.value?.showedBaseCurrency?.let { loadListActualCurrencyRates(it) }
    }

    fun handle(new: CurrenciesUserEvent) {
        when (new) {
            is CurrenciesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenOpen")
            }

            is CurrenciesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenClose")
            }

            is CurrenciesUserEvent.OnChangeBaseCurrency -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnChangeBaseCurrency")
                setShowedBaseCurrency(new.name)
                loadListActualCurrencyRates(new.name)
            }

            is CurrenciesUserEvent.OnChangeFavoriteState -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnChangeFavoriteState")
                updateListActualCurrencyRates(new.currency)
                if (new.currency.isFavorite) savePairToFavorite(new.currency)
                else deletePairFromFavorite(new.currency)
            }
        }
    }

    //TODO: bug if list > screen then dropdownmenu unsize
    private fun loadListBaseCurrencies() {
        logger.d(TAG_LOG, "$NAME_CLASS loadListBaseCurrencies(): start")
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
        logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): start")
        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine started")

                    val result = api.getRates(name)
                    logger.v(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine ended result = $result")

                    val list = result.rates?.getListRatesDto()?.asSequence()
                        ?.map { dto -> dto.toCurrency() }
                        ?.filterNot { model -> model.quotation == 0.0 }
                        ?.filterNot { model -> model.charCode.name == result.base }
                        ?.map { model -> model.toActualCurrencyRateUi() }
                        ?.toList() ?: emptyList()

                    _uiState.value = _uiState.value?.copy(listActualCurrencyRates = list)

                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine error $t", t)
            }
        }
    }


    private fun savePairToFavorite(currency: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_CLASS savePairToFavorite(): started")
        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_CLASS savePairToFavorite(): coroutine started")
                    _uiState.value?.let { state ->

                        val model = currency.toCurrencyPair(state.showedBaseCurrency)
                        val dbo = model.toFavoriteCurrencyPairDbo()
                        favoriteCurrencyPairApi.checkAndInsertUniquePair(dbo)
                    }

                    logger.v(TAG_LOG, "$NAME_CLASS savePairToFavorite(): coroutine ended")
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_CLASS saveFavoritePair(): coroutine error $t", t)
            }
        }
    }

    private fun updateListActualCurrencyRates(new: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_CLASS updateListActualCurrencyRates(): started")

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
        logger.d(TAG_LOG, "$NAME_CLASS deletePairFromFavorite(): started $currency")

        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_CLASS deletePairFromFavorite(): coroutine started")
                    _uiState.value?.let { state ->

                        val model = currency.toCurrencyPair(state.showedBaseCurrency)
                        val dbo = model.toFavoriteCurrencyPairDbo()
                        favoriteCurrencyPairApi.deleteAll(dbo)
                    }
                    logger.v(TAG_LOG, "$NAME_CLASS deletePairFromFavorite(): coroutine ended")
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_CLASS deletePairFromFavorite(): coroutine error $t", t)
            }
        }
    }


    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_CLASS onCleared(): started")
        super.onCleared()
    }
}