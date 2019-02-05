package com.fetcher

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) = runBlocking {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/price/{ticker}") {
            val ticker: String? = call.parameters["ticker"]
            if (ticker != null) {
               val request = async {
                   val price = IEXClient().retrievePriceAsync(ticker)?.price

                    if (price != null) {
                       call.respondText("$ticker current price is $$price")
                   } else {
                       call.respondText("Could not find price for $ticker")
                   }
               }
               request.await()
            }
            else {
                call.respondText("Invalid request")
            }
        }
    }
}