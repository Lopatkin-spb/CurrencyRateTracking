package com.example.currencyratetracking.presentation.currencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractViewModel
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG


internal class CurrenciesViewModel(
    private val logger: BaseLogger,
) : AbstractViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Currencies Fragment"
    }
    val text: LiveData<String> = _text

    init {
        logger.d(TAG_LOG, "$NAME_CLASS init(): started")
    }

    fun handle(new: CurrenciesUserEvent) {
        when (new) {
            is CurrenciesUserEvent.OnScreenOpen -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenOpen")
            }

            is CurrenciesUserEvent.OnScreenClose -> {
                logger.i(TAG_LOG, "$NAME_CLASS handle(): OnScreenClose")
            }
        }
    }

    override fun onCleared() {
        logger.d(TAG_LOG, "$NAME_CLASS onCleared(): started")
        super.onCleared()
    }
}