package com.example.currencyratetracking.presentation.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG


internal class CurrenciesViewModel(
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
                loadActualList()
            }

            is CurrenciesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenClose")
            }
        }
    }

    private fun loadActualList() {
        logger.i(TAG_LOG, "$NAME_CLASS loadActualList(): start")
        val listStub = mutableListOf<ActualCurrencyPair>()
        for (index in 1L..5) {
            val item = ActualCurrencyPair(
                id = index,
                name = "SDDF",
                quotation = 3.932455,
            )
            listStub.add(item)
        }
        _uiState.value = _uiState.value?.copy(listActual = listStub)
    }

    override fun onCleared() {
        logger.d(TAG_LOG, "$NAME_CLASS onCleared(): started")
        super.onCleared()
    }
}