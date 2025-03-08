package com.example.currencyratetracking.api_locale.api

import androidx.room.*


@Dao
interface FavoriteCurrencyPairApi {

    @Query("SELECT * FROM $TABLE_FAVORITE_CURRENCY_PAIR")
    suspend fun getAllPairs(): List<FavoriteCurrencyPairDbo>

    @Query("SELECT * FROM $TABLE_FAVORITE_CURRENCY_PAIR WHERE $COLUMN_CHAR_CODE_BASE = :charCodeBase")
    suspend fun getAllPairsBy(charCodeBase: String): List<FavoriteCurrencyPairDbo>

    @Query(
        "SELECT * FROM $TABLE_FAVORITE_CURRENCY_PAIR " +
                "WHERE $COLUMN_CHAR_CODE_BASE = :charCodeBase AND $COLUMN_CHAR_CODE_SECOND = :charCodeSecond"
    )
    suspend fun getAllPairsBy(charCodeBase: String, charCodeSecond: String): List<FavoriteCurrencyPairDbo>

    @Query(
        "SELECT * FROM $TABLE_FAVORITE_CURRENCY_PAIR " +
                "WHERE $COLUMN_CHAR_CODE_BASE = :charCodeBase AND $COLUMN_CHAR_CODE_SECOND = :charCodeSecond"
    )
    suspend fun getPairBy(charCodeBase: String, charCodeSecond: String): FavoriteCurrencyPairDbo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPair(model: FavoriteCurrencyPairDbo)

    @Delete
    suspend fun deletePairBy(model: FavoriteCurrencyPairDbo)

    @Query("DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR WHERE $COLUMN_ID = :pairId")
    suspend fun deletePairBy(pairId: Long)

    @Query(
        "DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR " +
                "WHERE $COLUMN_CHAR_CODE_BASE = :charCodeBase AND $COLUMN_CHAR_CODE_SECOND = :charCodeSecond"
    )
    suspend fun deletePairBy(charCodeBase: String, charCodeSecond: String)

    @Query("DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR")
    suspend fun clean()

    @Transaction
    suspend fun checkAndInsertUniquePair(model: FavoriteCurrencyPairDbo) {
        val list = getAllPairsBy(model.charCodeBase, model.charCodeSecond)
        if (list.isEmpty()) insertPair(model)
    }

    @Transaction
    suspend fun deleteAll(model: FavoriteCurrencyPairDbo) {
        deletePairBy(model.charCodeBase, model.charCodeSecond)
    }

}