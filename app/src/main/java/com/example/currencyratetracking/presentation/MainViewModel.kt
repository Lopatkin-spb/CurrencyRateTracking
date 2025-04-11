package com.example.currencyratetracking.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.presentation.AbstractViewModel
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class MainViewModel @AssistedInject constructor(
    private val logger: BaseLogger,
    private val dispatcher: BaseCoroutineDispatcher,
    private val clearUserSessionByLiveCycleUseCase: ClearUserSessionByLiveCycleUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : AbstractViewModel() {

    @AssistedFactory
    interface Factory : ViewModelAssistedSavedStateFactory<MainViewModel>

    //todo: rename
    companion object {
        private const val CLEAR_USER_SESSION_KEY: String =
            "com.example.currencyratetracking.presentation.CLEAR_USER_SESSION_KEY"
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, cause ->
        logger.e(TAG_LOG, "$NAME_CLASS CoroutineExceptionHandler: $coroutineContext", cause)
    }

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")
    }

    fun handle(new: MainUserEvent) {
        when (new) {
            is MainUserEvent.OnColdClose -> clearUserSession()
        }
    }

    private fun clearUserSession() {
        viewModelScope.launch(exceptionHandler + dispatcher.main() + CoroutineName(CLEAR_USER_SESSION_KEY)) {
            clearUserSessionByLiveCycleUseCase.execute()
                .onStart { logger.d(TAG_LOG, "$NAME_FULL onStart") }
                .flowOn(dispatcher.io())
                .onEach { logger.v(TAG_LOG, "$NAME_FULL result = $it") }
                .onCompletion { finally -> logger.d(TAG_LOG, "$NAME_FULL ended") }
                .collect()
        }
    }

    override fun onCleared() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onCleared()
    }

}