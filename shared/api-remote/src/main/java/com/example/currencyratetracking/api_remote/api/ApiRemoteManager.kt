package com.example.currencyratetracking.api_remote.api

import javax.inject.Inject


public interface ApiRemoteManager {

    public fun getRatesApi(): RatesApi

}

internal class ApiRemoteManagerImpl @Inject constructor(
    private val networkManager: NetworkManager,
) : ApiRemoteManager {

    override fun getRatesApi(): RatesApi {
        return networkManager.getModifiedRetrofit().create(RatesApi::class.java)
    }

}