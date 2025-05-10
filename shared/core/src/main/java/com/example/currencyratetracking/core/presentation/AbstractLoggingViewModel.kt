package com.example.currencyratetracking.core.presentation

import androidx.lifecycle.viewModelScope
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.Tag
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


abstract class AbstractLoggingViewModel<in E : UiEvent>(
    private val logger: BaseLogger,
    private val tag: Tag,
) : AbstractBaseViewModel<E>() {

    private var depth = 2

    protected val NAME_CLASS: String
        get() {
            val name = this.javaClass.simpleName
            return name
        }

    private val NAME_METHOD: String
        get() {
            val stackTrace = Throwable().stackTrace

            /**
             * normal name
             */
            var nameMethod = stackTrace[depth].methodName
            /**
             * check coroutine -> access$getNAME_METHOD
             */
            if (nameMethod.substringBefore("$") == "access") {
                //package.FavoritesViewModel$loadFavoritesList$1$1
                val package_Class_Method_Others = stackTrace[depth + 1].className
                //loadFavoritesList$1$1
                val Method_Others = package_Class_Method_Others.substringAfter("$")
                nameMethod = "${Method_Others.substringBefore("$")}.${stackTrace[depth + 1].methodName}"
            }
            return nameMethod
        }

    protected val NAME_FULL: String
        get() {
//            val name = "[$NAME_CLASS $NAME_METHOD()]"
            val name = "$NAME_CLASS $NAME_METHOD()::"
            return name
        }


    init {
        logger.d(tag.LOG, "$NAME_FULL started")
    }

    override fun handle(new: E) {
        logger.i(tag.LOG, "$NAME_FULL ${new.javaClass.simpleName}")
    }

    override fun handle(cause: Throwable, details: String) {
        when (cause) {
            is Exception -> logger.w(tag.LOG, "$NAME_FULL parent ${cause.message} $details", cause)
            else -> logger.e(tag.LOG, "$NAME_FULL parent ${cause.message} $details", cause)
        }
    }


    protected fun <T> Flow<T>.launchIn(
        context: CoroutineContext,
        funLogName: String,
    ): Job {
        return launchIn(
            scope = viewModelScope,
            context = context,
            funLogName = funLogName,
        )
    }

    protected fun <T> Flow<T>.launchIn(
        scope: CoroutineScope,
        context: CoroutineContext,
        funLogName: String,
    ): Job {
        return scope.launch {

            onStart { logger.d(tag.LOG, "$funLogName onStart") }

                .onEach { logger.v(tag.LOG, "$funLogName success") }

                .onCompletion {
                    if (it is CancellationException) {
                        logger.v(tag.LOG, "$funLogName cancel")
                    }
                    logger.d(tag.LOG, "$funLogName ended")
                }
                .collect() // tail-call
        }
    }

    override fun onCleared() {
        logger.d(tag.LOG, "$NAME_FULL started")
        super.onCleared()
    }

}