package com.example.currencyratetracking.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Inject


/**
 * The main entry point to work with serializations.
 * Returns an serialization instances.
 */
public interface SerializationManager {

    /**
     * Returns an default Json instance.
     */
    public fun getDefaultJson(): Json

    /**
     * Returns an modified Json instance with ignoreUnknownKeys.
     */
    public fun getIgnoreKeysJson(): Json

    /**
     * Returns an modified Json instance with ignoreUnknownKeys, prettyPrint, explicitNulls, coerceInputValues.
     */
    public fun getUpdatedJson(): Json

}

internal class SerializationManagerImpl @Inject constructor() : SerializationManager {

    override fun getDefaultJson(): Json {
        return Json
    }

    override fun getIgnoreKeysJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun getUpdatedJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            prettyPrint = true //up readability
            explicitNulls = false //prevent serializing "null" to field
            coerceInputValues = true //converting noncondition inner value to default value
        }
    }

}