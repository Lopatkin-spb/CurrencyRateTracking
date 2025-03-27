package com.example.currencyratetracking.api_locale.api.rate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface CurrencyPairRateApi {

    /**
     * return list or empty list
     */
    @Query("SELECT * FROM $TABLE_CURRENCY_PAIR_RATES WHERE ${COLUMN_CHAR_CODE_BASE} = :charCodeBase")
    suspend fun getAllPairsBy(charCodeBase: String): List<CurrencyPairRateDbo>

    /**
     * return inserted item id
     */
    @Insert
    suspend fun insertPair(model: CurrencyPairRateDbo): Long

    /**
     * return deleted items
     */
    @Query("DELETE FROM $TABLE_CURRENCY_PAIR_RATES")
    suspend fun clean(): Int
}