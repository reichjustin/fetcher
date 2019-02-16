package com.fetcher.dtos

import com.google.gson.annotations.SerializedName

data class StockQuote(@SerializedName("symbol") val symbol: String,
                      @SerializedName("companyName") val companyName: String,
                      @SerializedName("close") val close: Double)