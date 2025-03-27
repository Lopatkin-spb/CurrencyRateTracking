package com.example.currencyratetracking.api_locale.storage.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_locale.api.favorite.FavoriteCurrencyPairDbo
import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateApi
import com.example.currencyratetracking.api_locale.api.rate.CurrencyPairRateDbo
import javax.inject.Inject


interface DatabaseApiManager {

    fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi

    fun getCurrencyPairRateApi(): CurrencyPairRateApi

}


internal class DatabaseApiManagerImpl @Inject constructor(private val context: Context) : DatabaseApiManager {

    override fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi {
        return AppRoomDatabase.getInstance(context).getFavoriteCurrencyPairApi()
    }

    override fun getCurrencyPairRateApi(): CurrencyPairRateApi {
        return AppRoomDatabase.getInstance(context).getCurrencyPairRateApi()
    }
}


@Database(
    version = 2,
    exportSchema = false,
    entities = [
        FavoriteCurrencyPairDbo::class,
        CurrencyPairRateDbo::class,
    ],
)
internal abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi

    abstract fun getCurrencyPairRateApi(): CurrencyPairRateApi


    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppRoomDatabase {
            return Room.databaseBuilder(
                context = requireNotNull(context.applicationContext),
                klass = AppRoomDatabase::class.java,
                name = "app_room_database.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}