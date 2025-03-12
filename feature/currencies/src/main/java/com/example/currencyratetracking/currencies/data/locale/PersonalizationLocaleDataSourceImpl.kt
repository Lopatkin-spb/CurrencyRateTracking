package com.example.currencyratetracking.currencies.data.locale

import com.example.currencyratetracking.api_locale.api.PersonalizationApi
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractDataSource
import com.example.currencyratetracking.currencies.ModuleTag
import com.example.currencyratetracking.currencies.data.locale.dataSource.PersonalizationLocaleDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


internal class PersonalizationLocaleDataSourceImpl @Inject constructor(
    private val personalizationApi: PersonalizationApi,
    private val logger: BaseLogger,
) : PersonalizationLocaleDataSource, AbstractDataSource() {

    override fun getUserSelectedBaseCurrency(): Flow<String> {
        return flow {
            logger.v(ModuleTag.TAG_LOG, "$NAME_FULL started")
            emit(personalizationApi.getUserSelectedBaseCurrency())
        }
    }

    override fun setUserSelectedBaseCurrency(base: String): Flow<Boolean> {
        return flow {
            logger.v(ModuleTag.TAG_LOG, "$NAME_FULL started")
            emit(personalizationApi.setUserSelectedBaseCurrency(base))
        }
    }
}