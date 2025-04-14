package com.example.currencyratetracking.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.catchCancellation
import com.example.currencyratetracking.core.catchException
import com.example.currencyratetracking.core.presentation.AbstractLoggingViewModel
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.core.transformToList
import com.example.currencyratetracking.favorites.ModuleTag.TAG_LOG
import com.example.currencyratetracking.favorites.domain.DeletePairCurrenciesFromFavoriteByCharCodesUseCase
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.SetPairCurrenciesToFavoriteUseCase
import com.example.currencyratetracking.model.CurrencyUi
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
) : AbstractLoggingViewModel<FavoritesUiEvent>() {

    @AssistedFactory
    interface Factory : ViewModelAssistedSavedStateFactory<FavoritesViewModel>

    companion object {
        private const val NAME_LOAD_LIST_FAVORITE_PAIRS: String =
            "com.example.currencyratetracking.favorites.presentation.NAME_LOAD_LIST_FAVORITE_PAIRS"
        private const val NAME_DELETE_PAIR_FROM_FAVORITE: String =
            "com.example.currencyratetracking.favorites.presentation.NAME_DELETE_PAIR_FROM_FAVORITE"
        private const val NAME_SAVE_PAIR_TO_FAVORITE: String =
            "com.example.currencyratetracking.favorites.presentation.NAME_SAVE_PAIR_TO_FAVORITE"
    }

    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState: LiveData<FavoritesUiState> = _uiState
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, cause ->
        logger.e(TAG_LOG, "$NAME_CLASS CoroutineExceptionHandler: $coroutineContext", cause)
    }

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")
    }

    override fun handle(new: FavoritesUiEvent) {
        logger.i(TAG_LOG, "$NAME_FULL ${new.javaClass.simpleName}")

        when (new) {
            is FavoritesUiEvent.OnScreenOpen -> loadFavoritesList()
            is FavoritesUiEvent.OnScreenClose -> {}
            is FavoritesUiEvent.OnChangeFavoriteState -> {
                if (new.currency.isFavorite) savePairToFavorite(new.currency)
                else deletePairFromFavorite(new.currency)
            }
        }
    }

    private fun loadFavoritesList() {
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(NAME_LOAD_LIST_FAVORITE_PAIRS)) {
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
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(NAME_DELETE_PAIR_FROM_FAVORITE)) {
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
        viewModelScope.launch(dispatcher.main() + exceptionHandler + CoroutineName(NAME_SAVE_PAIR_TO_FAVORITE)) {
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
        logger.d(TAG_LOG, "$NAME_FULL started")
        super.onCleared()
    }

}