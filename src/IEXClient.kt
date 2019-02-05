package com.fetcher.iex

import com.fetcher.dtos.StockPrice
import io.github.rybalkinsd.kohttp.ext.asyncHttpGet

interface IClient {
    suspend fun retrievePriceAsync(ticker: String): StockPrice?
}

object IEXClient : IClient {
    private val _url = "https://api.iextrading.com/1.0/"

    override suspend fun retrievePriceAsync(ticker: String): StockPrice? {
        val url = "$_url/stock/$ticker/price"

        val response = url.asyncHttpGet()

        response.await().use {
            val price: Double? = it.body()?.string()?.toDouble()
            if (price != null) {
                return StockPrice(price)
            }
            return null
        }
    }
}