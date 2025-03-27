package com.example.currencyratetracking.api_locale.api.rate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = TABLE_CURRENCY_PAIR_RATES)
data class CurrencyPairRateDbo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(COLUMN_CHAR_CODE_BASE) val charCodeBase: String,
    @ColumnInfo(COLUMN_CHAR_CODE_SECOND) val charCodeSecond: String,
    @ColumnInfo(COLUMN_QUOTATION) val quotation: Double,
)


internal const val TABLE_CURRENCY_PAIR_RATES = "currency_pair_rates_table"

internal const val COLUMN_ID = "id"
internal const val COLUMN_CHAR_CODE_BASE = "char_code_base"
internal const val COLUMN_CHAR_CODE_SECOND = "char_code_second"
internal const val COLUMN_QUOTATION = "quotation"