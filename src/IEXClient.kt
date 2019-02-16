package com.fetcher.iex

import com.fetcher.dtos.StockQuote
import com.google.gson.Gson
import io.github.rybalkinsd.kohttp.ext.asyncHttpGet
import com.typesafe.config.ConfigFactory




interface IClient {
    suspend fun retrievePriceAsync(ticker: String): StockQuote?
}

object IEXClient : IClient {

    override suspend fun retrievePriceAsync(ticker: String): StockQuote? {
        val conf = ConfigFactory.load()
        val _url = conf.getString("secrets.iex.url")
        val _token = conf.getString("secrets.iex.token")

        val url = "${_url}/stock/${ticker}/quote?token=${_token}"


        val response = url.asyncHttpGet()

        response.await().use {
            val response: String?  = it.body()?.string()

            if (response != null) {
                val quote = Gson().fromJson(response, StockQuote::class.java)

                return quote
            }

            return null
        }
    }
}