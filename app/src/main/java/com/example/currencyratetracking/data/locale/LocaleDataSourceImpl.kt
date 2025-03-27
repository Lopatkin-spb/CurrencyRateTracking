package com.example.currencyratetracking.data.locale

import com.example.currencyratetracking.api_locale.storage.application.DatabaseApiManager
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.presentation.ModuleTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class LocaleDataSourceImpl @Inject constructor(
    private val databaseApiManager: DatabaseApiManager,
    private val logger: BaseLogger,
) : AbstractDataSource(), LocaleDataSource {

    override fun clearUserSession(): Flow<Boolean> {
        return flow {
            val items = databaseApiManager.getCurrencyPairRateApi().clean()
            logger.v(ModuleTag.TAG_LOG, "$NAME_FULL items deleted = $items")
            emit(items > 0)
        }
    }
}