package com.example.currencyratetracking.api_locale.api.favorite

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
    suspend fun getPairBy(charCodeBase: String, charCodeSecond: String): FavoriteCurrencyPairDbo?

    /**
     * return deleted item id
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPair(model: FavoriteCurrencyPairDbo): Long

    @Delete
    suspend fun deletePairBy(model: FavoriteCurrencyPairDbo)

    @Query("DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR WHERE $COLUMN_ID = :pairId")
    suspend fun deletePairBy(pairId: Long)

    /**
     * return number deleted items
     */
    @Query(
        "DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR " +
                "WHERE $COLUMN_CHAR_CODE_BASE = :charCodeBase AND $COLUMN_CHAR_CODE_SECOND = :charCodeSecond"
    )
    suspend fun deletePairBy(charCodeBase: String, charCodeSecond: String): Int

    @Query("DELETE FROM $TABLE_FAVORITE_CURRENCY_PAIR")
    suspend fun clean()

    /**
     * return true or false
     */
    @Transaction
    suspend fun checkAndInsertUniquePair(model: FavoriteCurrencyPairDbo): Boolean {
        val list = getAllPairsBy(model.charCodeBase, model.charCodeSecond)
        if (list.isEmpty()) {
            val id = insertPair(model)
            return id > 0
        }
        return false
    }

    /**
     * return true or false
     */
    //TODO: add custom exception - check to downstream & update ui
    @Transaction
    suspend fun deleteAll(model: FavoriteCurrencyPairDbo): Boolean {
        deletePairBy(model.charCodeBase, model.charCodeSecond)
        val pairDbo = getPairBy(model.charCodeBase, model.charCodeSecond)
        return pairDbo == null
    }

    /**
     * return true or false
     */
    @Transaction
    suspend fun checkPairBy(model: FavoriteCurrencyPairDbo): Boolean {
        val pairDbo = getPairBy(model.charCodeBase, model.charCodeSecond)
        return pairDbo != null
    }

}