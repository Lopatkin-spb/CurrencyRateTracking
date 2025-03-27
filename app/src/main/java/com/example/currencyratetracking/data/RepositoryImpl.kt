package com.example.currencyratetracking.data

import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractRepository
import com.example.currencyratetracking.data.locale.LocaleDataSource
import com.example.currencyratetracking.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class RepositoryImpl @Inject constructor(
    private val logger: BaseLogger,
    private val localeDataSource: LocaleDataSource,
) : AbstractRepository(), Repository {

    override fun clearUserSession(): Flow<Boolean> {
        return localeDataSource.clearUserSession()
    }
}