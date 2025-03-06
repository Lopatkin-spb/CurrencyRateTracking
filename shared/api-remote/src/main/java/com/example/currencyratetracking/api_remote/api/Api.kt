package com.example.currencyratetracking.api_remote.api

import retrofit2.http.GET
import retrofit2.http.Query


public interface RatesApi {

    @GET("rates?base=USD")
    public suspend fun getRates(): ResponseDto

    /**
     * Example "rates?base=USD"
     */
    @GET("rates")
    public suspend fun getRates(@Query("base") targetCurrency: String): ResponseDto

}
