package com.example.currencyratetracking.presentation.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.api_remote.api.RatesApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.model.CurrencyInfo
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import com.example.currencyratetracking.presentation.toCurrency
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


internal class CurrenciesViewModel(
    private val api: RatesApi,
    private val logger: BaseLogger,
) : AbstractViewModel() {

    private val _uiState = MutableLiveData(CurrenciesUiState())
    val uiState: LiveData<CurrenciesUiState> = _uiState

    init {
        logger.d(TAG_LOG, "$NAME_CLASS init(): started")
    }

    fun handle(new: CurrenciesUserEvent) {
        when (new) {
            is CurrenciesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenOpen")
                loadListBaseCurrencies()
                _uiState.value?.showedBaseCurrency?.let { loadListActualCurrencyRates(it) }
            }

            is CurrenciesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenClose")
            }

            is CurrenciesUserEvent.OnChangeBaseCurrency -> {
                setShowedBaseCurrency(new.name)
                loadListActualCurrencyRates(new.name)
            }

            is CurrenciesUserEvent.OnSaveToFavorite -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnSaveToFavorite ${new.currency} ${new.saveState}")
            }
        }
    }

    //TODO: bug if list > screen then dropdownmenu unsize
    private fun loadListBaseCurrencies() {
        logger.i(TAG_LOG, "$NAME_CLASS loadListBaseCurrencies(): start")
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

    private fun loadListActualCurrencyRates() {
        logger.i(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): start")

        runBlocking {

            try {
                launch {
                    logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine started")

                    val result = api.getRates()
                    logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine ended result = $result")

                    val list = result.rates?.getListRatesDto()?.asSequence()
                        ?.map { dto -> dto.toCurrency() }
                        ?.filterNot { model -> model.quotation == 0.0 }
                        ?.map { model -> model.toActualCurrencyRateUi() }
                        ?.toList() ?: emptyList()

                    _uiState.value = _uiState.value?.copy(listActualCurrencyRates = list)
                }
            } catch (t: Throwable) {
                logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine error $t")
            }
        }

    }

    private fun loadListActualCurrencyRates(name: String) {
        logger.i(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): start")
        runBlocking {

            try {
                launch {
                    logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine started")

                    val result = api.getRates(name)
                    logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine ended result = $result")

                    val list = result.rates?.getListRatesDto()?.asSequence()
                        ?.map { dto -> dto.toCurrency() }
                        ?.filterNot { model -> model.quotation == 0.0 }
                        ?.filterNot { model -> model.charCode.name == result.base }
                        ?.map { model -> model.toActualCurrencyRateUi() }
                        ?.toList() ?: emptyList()

                    _uiState.value = _uiState.value?.copy(listActualCurrencyRates = list)
                }
            } catch (t: Throwable) {
                logger.d(TAG_LOG, "$NAME_CLASS loadListActualCurrencyRates(): coroutine error $t")
            }
        }
    }

    override fun onCleared() {
        logger.d(TAG_LOG, "$NAME_CLASS onCleared(): started")
        super.onCleared()
    }
}