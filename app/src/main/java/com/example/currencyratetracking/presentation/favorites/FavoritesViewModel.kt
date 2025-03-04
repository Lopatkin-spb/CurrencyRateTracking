package com.example.currencyratetracking.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG


internal class FavoritesViewModel(
    private val logger: BaseLogger,
) : AbstractViewModel() {

    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState

    init {
        logger.d(TAG_LOG, "$NAME_CLASS init(): started")
    }

    fun handle(new: FavoritesUserEvent) {
        when (new) {
            is FavoritesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenOpen")
                loadFavoritesList()
            }

            is FavoritesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenClose")
            }
        }
    }

    private fun loadFavoritesList() {
        logger.i(TAG_LOG, "$NAME_CLASS loadFavoritesList(): start")
        val listStub = mutableListOf<FavoriteCurrencyPair>()
        for (index in 1L..3) {
            val item = FavoriteCurrencyPair(
                id = index,
                name = "SDDF",
                quotation = 3.932455,
            )
            listStub.add(item)
        }
        _uiState.value = _uiState.value?.copy(listFavorites = listStub)
    }

    override fun onCleared() {
        logger.d(TAG_LOG, "$NAME_CLASS onCleared(): started")
        super.onCleared()
    }

}