package com.example.currencyratetracking.favorites.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.Tag
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.presentation.AbstractLoggingViewModel
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.core.transformToList
import com.example.currencyratetracking.favorites.domain.DeletePairCurrenciesFromFavoriteByCharCodesUseCase
import com.example.currencyratetracking.favorites.domain.GetListFavoritePairsUseCase
import com.example.currencyratetracking.favorites.domain.SetPairCurrenciesToFavoriteUseCase
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*


class FavoritesViewModel @AssistedInject constructor(
    private val getListFavoritePairsUseCase: GetListFavoritePairsUseCase,
    private val dispatcher: BaseCoroutineDispatcher,
    private val logger: BaseLogger,
    private val setPairCurrenciesToFavoriteUseCase: Lazy<SetPairCurrenciesToFavoriteUseCase>,
    private val deletePairCurrenciesFromFavoriteByCharCodesUseCase: Lazy<DeletePairCurrenciesFromFavoriteByCharCodesUseCase>,
    private val tag: Tag,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : AbstractLoggingViewModel<FavoritesUiEvent>(logger, tag) {

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

    private val _uiState: MutableStateFlow<FavoritesUiState> = MutableStateFlow(FavoritesUiState.Empty)
    val uiState: StateFlow<FavoritesUiState>
        get() = _uiState.asStateFlow()

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, cause -> handle(cause, "$coroutineContext") }

    init {
        loadFavoritesList()
    }

    override fun handle(new: FavoritesUiEvent) {
        super.handle(new)

        when (new) {
            is FavoritesUiEvent.OnScreenOpen -> {}
            is FavoritesUiEvent.OnScreenClose -> {}
            is FavoritesUiEvent.OnChangeFavoriteState -> {
                if (new.currency.isFavorite) savePairToFavorite(new.currency.toFavoritePairCurrenciesRateUi())
                else deletePairFromFavorite(new.currency.toFavoritePairCurrenciesRateUi())
            }
        }
    }

    private fun loadFavoritesList() {
        getListFavoritePairsUseCase.execute()
            .onStart { _uiState.update { FavoritesUiState.Loading } }
            .map { model -> model.toFavoritePairCurrenciesRateUi() }
            .transformToList()
            .cancellable()
            .onEach { list -> _uiState.update { FavoritesUiState.Success(listFavorites = list) } }
            .onCompletion { if (it is CancellationException) _uiState.update { FavoritesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_LOAD_LIST_FAVORITE_PAIRS),
                funLogName = NAME_FULL,
            )
    }

    private fun deletePairFromFavorite(currency: FavoritePairCurrenciesRateUi) {
        deletePairCurrenciesFromFavoriteByCharCodesUseCase.get().execute(currency.text)
            .map { result -> currency.copy(isFavorite = !result) }
            .onEach { model -> updateFavoritePairState(model) }
            .onCompletion { if (it is CancellationException) _uiState.update { FavoritesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_DELETE_PAIR_FROM_FAVORITE),
                funLogName = NAME_FULL,
            )
    }

    private fun savePairToFavorite(currency: FavoritePairCurrenciesRateUi) {
        setPairCurrenciesToFavoriteUseCase.get().execute(currency.text)
            .map { result -> currency.copy(isFavorite = result) }
            .onEach { model -> updateFavoritePairState(model) }
            .onCompletion { if (it is CancellationException) _uiState.update { FavoritesUiState.Error(text = "Cancellation error") } }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_SAVE_PAIR_TO_FAVORITE),
                funLogName = NAME_FULL,
            )
    }

    private fun updateFavoritePairState(new: FavoritePairCurrenciesRateUi) {
        _uiState.update { currentState ->
            val newList = arrayListOf<FavoritePairCurrenciesRateUi>()

            currentState.asSuccess().listFavorites.forEach { item ->
                if (item.id == new.id) {
                    newList.add(new)
                } else {
                    newList.add(item)
                }
            }
            FavoritesUiState.Success(listFavorites = newList)
        }
    }

    override fun handle(cause: Throwable, details: String) {
        super.handle(cause, details)
        _uiState.update { FavoritesUiState.Error(text = cause.message ?: "Unspecified error") }
        //& next - errors send to analytic
    }

}