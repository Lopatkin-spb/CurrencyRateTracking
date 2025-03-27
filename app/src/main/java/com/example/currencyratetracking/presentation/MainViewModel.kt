package com.example.currencyratetracking.presentation

import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


internal class MainViewModel(
    private val logger: BaseLogger,
    private val dispatcher: BaseCoroutineDispatcher,
    private val clearUserSessionByLiveCycleUseCase: ClearUserSessionByLiveCycleUseCase,
) : AbstractViewModel() {

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
            is MainUserEvent.OnCloseApp -> clearUserSession()
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