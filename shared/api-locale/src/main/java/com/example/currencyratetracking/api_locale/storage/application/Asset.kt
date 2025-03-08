package com.example.currencyratetracking.api_locale.storage.application

import android.content.Context
import java.io.InputStream
import javax.inject.Inject


interface AssetManager {

    fun open(fileName: String): InputStream

}


internal class AndroidAssetManager @Inject constructor(private val context: Context) : AssetManager {

    override fun open(fileName: String): InputStream {
        return context.assets.open(fileName)
    }

}