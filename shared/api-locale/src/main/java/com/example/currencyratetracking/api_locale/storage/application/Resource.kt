package com.example.currencyratetracking.api_locale.storage.application

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import javax.inject.Inject


interface ResourceManager {

    fun getString(@StringRes id: Int): String

    @ColorInt
    fun getColor(@ColorRes id: Int): Int
}

internal class AndroidResourceManager @Inject constructor(private val context: Context) : ResourceManager {

    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getColor(id: Int): Int {
        return context.getColor(id)
    }

}