package com.example.currencyratetracking.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.*
import com.example.currencyratetracking.core.presentation.AbstractViewModel
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.favorites.ModuleTag.TAG_LOG
import com.example.currencyratetracking.favorites.domain.DeletePairCurrenciesFromFavoriteByCharCodesUseCase
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.SetPairCurrenciesToFavoriteUseCase
import com.example.currencyratetracking.model.CurrencyUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.Lazy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class FavoritesViewModel @AssistedInject constructor(
    private val getListFavoritePairsUseCase: GetListFavoritePairsUseCase,
    private val dispatcher: BaseCoroutineDispatcher,
    private val logger: BaseLogger,
    private val setPairCurrenciesToFavoriteUseCase: Lazy<SetPairCurrenciesToFavoriteUseCase>,
    private val deletePairCurrenciesFromFavoriteByCharCodesUseCase: Lazy<DeletePairCurrenciesFromFavoriteByCharCodesUseCase>,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : AbstractViewModel() {

    @AssistedFactory
    interface Factory : ViewModelAssistedSavedStateFactory<FavoritesViewModel>

    companion object {
        //todo: rename
        private const val LOAD_LIST_FAVORITE_PAIRS_KEY: String =
            "com.example.currencyratetracking.favorites.presentation.LOAD_LIST_FAVORITE_PAIRS_KEY"
        private const val DELETE_PAIR_FROM_FAVORITE_KEY: String =
            "com.example.currencyratetracking.favorites.presentation.DELETE_PAIR_FROM_FAVORITE_KEY"
        private const val SAVE_PAIR_TO_FAVORITE_KEY: String =
            "com.example.currencyratetracking.favorites.presentation.SAVE_PAIR_TO_FAVORITE_KEY"
    }

    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, cause ->
        logger.e(TAG_LOG, "$NAME_CLASS CoroutineExceptionHandler: $coroutineContext", cause)
    }

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")

        //todo: remove
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
                if (new.currency.isFavorite) savePairToFavorite(new.currency)
                else deletePairFromFavorite(new.currency)
            }
        }
    }

    private fun loadFavoritesList() {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(LOAD_LIST_FAVORITE_PAIRS_KEY)) {
            getListFavoritePairsUseCase.execute()
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .map { model -> model.toFavoritePairCurrenciesRateUi() }
                .transformToList()
                .cancellable()
                .flowOn(dispatcher.io())
                .onEach { list ->
                    _uiState.value = _uiState.value?.copy(listFavorites = list)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }

    private fun deletePairFromFavorite(currency: CurrencyUi) {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(DELETE_PAIR_FROM_FAVORITE_KEY)) {
            deletePairCurrenciesFromFavoriteByCharCodesUseCase.get().execute(currency.text)
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
                    updateFavoritePairState(model)
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
            setPairCurrenciesToFavoriteUseCase.get().execute(currency.text)
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
                    updateFavoritePairState(model)
                    logger.v(TAG_LOG, "$NAME_FULL success")
                }
                .catchCancellation { logger.v(TAG_LOG, "$NAME_FULL cancel") }
                .catchException { logger.w(TAG_LOG, "$NAME_FULL ${it.message}", it) }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }

    //todo: must better speed
    private fun updateFavoritePairState(new: CurrencyUi) {
        logger.v(TAG_LOG, "$NAME_FULL started")

        val newList = arrayListOf<FavoritePairCurrenciesRateUi>()
        _uiState.value?.listFavorites?.let { list ->
            list.forEach { item ->
                if (item.id == new.id) {
                    newList.add(
                        FavoritePairCurrenciesRateUi(
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

    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onCleared()
    }

}