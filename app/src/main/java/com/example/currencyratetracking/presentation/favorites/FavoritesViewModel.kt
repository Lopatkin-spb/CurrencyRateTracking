package com.example.currencyratetracking.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.model.CurrencyUi
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import com.example.currencyratetracking.presentation.toCurrencyPair
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class FavoritesViewModel @Inject constructor(
    private val logger: BaseLogger,
    private val databaseApiManager: DatabaseApiManager,
) : AbstractViewModel() {

    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")

        loadFavoritesList()
    }

    fun handle(new: FavoritesUserEvent) {
        when (new) {
            is FavoritesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_FULL OnScreenOpen")
            }

            is FavoritesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_FULL OnScreenClose")
            }

            is FavoritesUserEvent.OnChangeFavoriteState -> {
                logger.i(TAG_LOG, "$NAME_FULL OnChangeFavoriteState")
                updateFavoritePairState(new.currency)
            }
        }
    }

    //TODO: renames
    private fun loadFavoritesList() {
        logger.d(TAG_LOG, "$NAME_FULL started")

        runBlocking {

            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_FULL started")

                    val list = databaseApiManager.getFavoriteCurrencyPairApi().getAllPairs().asSequence()
                        .map { dbo -> dbo.toCurrencyPair() }
                        .map { model -> model.toFavoriteCurrencyRate() }
                        .toList()


                    _uiState.value = _uiState.value?.copy(listFavorites = list)

                    logger.v(TAG_LOG, "$NAME_FULL ended")
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_FULL error $t", t)
            }
        }
    }

    private fun updateFavoritePairState(new: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_FULL started")

        val newList = arrayListOf<FavoriteCurrencyRate>()
        _uiState.value?.listFavorites?.let { list ->
            list.forEach { item ->
                if (item.id == new.id) {
                    newList.add(
                        FavoriteCurrencyRate(
                            id = new.id,
                            text = new.text,
                            quotation = new.quotation,
                            isFavorite = new.isFavorite,
                        )
                    )
                } else {
                    newList.add(item)
                }
            }
        }
        _uiState.value = _uiState.value?.copy(listFavorites = newList)
    }

    private fun updateFavoritePairsToDb() {
        _uiState.value?.listFavorites?.let { list ->
            list.forEach { item ->
                if (!item.isFavorite) deletePairFromFavorite(item)
            }
        }
    }


    private fun deletePairFromFavorite(currency: CurrencyUi) {
        logger.d(TAG_LOG, "$NAME_FULL started")

        runBlocking {
            try {
                launch {
                    logger.v(TAG_LOG, "$NAME_FULL started")

                    if (!currency.isFavorite) databaseApiManager.getFavoriteCurrencyPairApi().deletePairBy(currency.id)
                    logger.v(TAG_LOG, "$NAME_FULL ended")
                    loadFavoritesList()
                }
            } catch (t: Throwable) {
                logger.w(TAG_LOG, "$NAME_FULL error $t", t)
            }
        }
    }

    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        updateFavoritePairsToDb()
        super.onCleared()
    }

}