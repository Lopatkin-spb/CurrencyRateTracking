package com.example.currencyratetracking.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.BaseCoroutineDispatcher
import com.example.currencyratetracking.core.presentation.AbstractLoggingViewModel
import com.example.currencyratetracking.core.presentation.ViewModelAssistedSavedStateFactory
import com.example.currencyratetracking.domain.ClearUserSessionByLiveCycleUseCase
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*


class MainViewModel @AssistedInject constructor(
    private val logger: BaseLogger,
    private val dispatcher: BaseCoroutineDispatcher,
    private val clearUserSessionByLiveCycleUseCase: ClearUserSessionByLiveCycleUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : AbstractLoggingViewModel<MainUserEvent>() {

    @AssistedFactory
    interface Factory : ViewModelAssistedSavedStateFactory<MainViewModel>

    companion object {
        private const val NAME_CLEAR_USER_SESSION: String =
            "com.example.currencyratetracking.presentation.NAME_CLEAR_USER_SESSION"
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, cause -> handle(cause, "$coroutineContext") }

    init {
        logger.d(TAG_LOG, "$NAME_FULL started")
    }

    override fun handle(new: MainUserEvent) {
        super.handle(new)

        when (new) {
            is MainUserEvent.OnColdClose -> clearUserSession()
        }
    }

    override fun handle(cause: Throwable, details: String) {
        super.handle(cause, details)
    }

    private fun clearUserSession() {
        clearUserSessionByLiveCycleUseCase.execute()
            .onEach { logger.v(TAG_LOG, "$NAME_FULL result = $it") }
            .launchIn(
                context = dispatcher.io() + exceptionHandler + CoroutineName(NAME_CLEAR_USER_SESSION),
                funLogName = NAME_FULL,
            )
    }

    override fun getLogger(): BaseLogger = logger

    override fun getTag(): String = TAG_LOG

}