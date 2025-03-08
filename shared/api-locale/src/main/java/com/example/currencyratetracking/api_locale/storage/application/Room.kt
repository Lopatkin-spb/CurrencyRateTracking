package com.example.currencyratetracking.api_locale.storage.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairApi
import com.example.currencyratetracking.api_locale.api.FavoriteCurrencyPairDbo
import javax.inject.Inject


interface DatabaseApiManager {

    fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi
}


internal class DatabaseApiManagerImpl @Inject constructor(private val context: Context) : DatabaseApiManager {

    override fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi {
        return AppRoomDatabase.getInstance(context).getFavoriteCurrencyPairApi()
    }
}


@Database(
    version = 1,
    entities = [FavoriteCurrencyPairDbo::class],
)
internal abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun getFavoriteCurrencyPairApi(): FavoriteCurrencyPairApi

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
            ).build()
        }
    }
}