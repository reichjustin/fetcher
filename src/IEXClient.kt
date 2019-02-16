package com.fetcher.iex

import com.fetcher.dtos.StockQuote
import com.google.gson.Gson
import io.github.rybalkinsd.kohttp.ext.asyncHttpGet
import com.typesafe.config.ConfigFactory
import okhttp3.ResponseBody

interface IClient {
    suspend fun retrievePriceAsync(ticker: String): StockQuote?
}

object IEXClient : IClient {

    private fun getStockQuoteRequestUrl(ticker: String): String {
        val conf = ConfigFactory.load()
        val baseUrl = conf.getString("secrets.iex.url")
        val token = conf.getString("secrets.iex.token")

       return "$baseUrl/stock/$ticker/quote?token=$token"
    }

    override suspend fun retrievePriceAsync(ticker: String): StockQuote? {
        val response = this.getStockQuoteRequestUrl(ticker).asyncHttpGet()

        response.await().use {
            val response: ResponseBody?  = it.body()

            if (response === null) {
                return null
            }

            return  Gson().fromJson(response.string(), StockQuote::class.java)
        }
    }
}