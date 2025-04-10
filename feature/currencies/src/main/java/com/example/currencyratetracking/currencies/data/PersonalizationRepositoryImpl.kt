package com.example.currencyratetracking.currencies.data

import com.example.currencyratetracking.core.data.AbstractRepository
import com.example.currencyratetracking.currencies.data.locale.dataSource.PersonalizationLocaleDataSource
import com.example.currencyratetracking.currencies.domain.repository.PersonalizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


internal class PersonalizationRepositoryImpl @Inject constructor(
    private val personalizationLocaleDataSource: PersonalizationLocaleDataSource,
) : PersonalizationRepository, AbstractRepository() {

    override fun getUserSelectedBaseCurrency(): Flow<String> {
        return personalizationLocaleDataSource.getUserSelectedBaseCurrency()
    }

    override fun setUserSelectedBaseCurrency(base: String): Flow<Boolean> {
        return personalizationLocaleDataSource.setUserSelectedBaseCurrency(base)
    }
}