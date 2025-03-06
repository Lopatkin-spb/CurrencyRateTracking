package com.example.currencyratetracking.api_remote.api

import com.example.currencyratetracking.common.SerializationManager
import okhttp3.CertificatePinner
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Retrofit adapts a Java interface to HTTP calls by using annotations on the declared methods
 * to define how requests are made.
 */
public interface NetworkManager {

    /**
     * Returns an default Retrofit instance. Only for check work!!
     */
    public fun getDefaultRetrofit(): Retrofit

    /**
     * Returns an modified Retrofit instance. For min normal work.
     */
    public fun getModifiedRetrofit(): Retrofit

}

internal class NetworkManagerImpl(
    private val client: ClientManager,
    private val converterFactory: ConverterFactoryManager,
    private val urlApi: String,
) : NetworkManager {

    override fun getDefaultRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlApi)
            .client(client.getDefaultOkHttp())
            .addConverterFactory(converterFactory.getDefaultJsonConverterFactory())
            .build()
    }

    override fun getModifiedRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlApi)
            .client(client.getCustomizedOkHttp())
            .addConverterFactory(converterFactory.getModifiedJsonConverterFactory())
            .build()
    }

}


/**
 * The HTTP clients used for requests.
 */
public interface ClientManager {

    /**
     * Returns an default OkHttpClient instance with only log intercepts.
     */
    public fun getDefaultOkHttp(): OkHttpClient

    /**
     * Returns an customized OkHttpClient instance with timeouts & log intercepts.
     */
    public fun getCustomizedOkHttp(): OkHttpClient

}

internal class ClientManagerImpl(
    private val buildType: String,
) : ClientManager {

    companion object {
        private val PROTOCOL_MAIN = Protocol.HTTP_2
        private val PROTOCOL_DEFAULT = Protocol.HTTP_1_1

        private const val CONNECTIONS_DEFAULT = 5
        private const val DURATION_DEFAULT = 5L

        private const val TIMEOUT_DEFAULT = 10L
        private const val TIMEOUT_NORMAL = 30L
    }

    override fun getDefaultOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor()) //Must last (bottom) after all interceptors for intercept all data
            .build()
    }

    override fun getCustomizedOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()

            //update pinner for better security: delete DEFAULT & create builder & added hardcode sha256
            .certificatePinner(
                certificatePinner = CertificatePinner.DEFAULT
            )

            .protocols(listOf(PROTOCOL_MAIN, PROTOCOL_DEFAULT))

            .connectionPool(
                connectionPool = ConnectionPool(
                    maxIdleConnections = CONNECTIONS_DEFAULT,
                    keepAliveDuration = DURATION_DEFAULT,
                    timeUnit = TimeUnit.MINUTES,
                )
            )

            .connectTimeout(TIMEOUT_NORMAL, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_NORMAL, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_NORMAL, TimeUnit.SECONDS)

            .addInterceptor(getLoggingInterceptor()) //Must last (bottom) after all interceptors for intercept all data
            .build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()

        if (buildType == "debug") {
            interceptor.level = HttpLoggingInterceptor.Level.BODY //min full info
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.BASIC //min info
        }

        return interceptor
    }

}


/**
 * Converter factory for serialization and deserialization of objects.
 */
public interface ConverterFactoryManager {

    public fun getDefaultJsonConverterFactory(): Converter.Factory

    public fun getModifiedJsonConverterFactory(): Converter.Factory

}

internal class ConverterFactoryManagerImpl @Inject constructor(
    private val serializer: SerializationManager,
) : ConverterFactoryManager {

    override fun getDefaultJsonConverterFactory(): Converter.Factory {
        return serializer.getDefaultJson().asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

    override fun getModifiedJsonConverterFactory(): Converter.Factory {
        return serializer.getUpdatedJson().asConverterFactory("application/json; charset=UTF8".toMediaType())
    }

}