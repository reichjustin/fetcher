package com.fetcher

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.*
import kotlinx.coroutines.async
import com.fetcher.iex.*
import kotlinx.coroutines.coroutineScope

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false)  {
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
                coroutineScope {
                    async {
                        val price = IEXClient.retrievePriceAsync(ticker)

                        if (price != null) {
                            call.respond(price)
                        } else {
                            call.respondText("Could not find price for $ticker")
                        }
                    }
                }
            } else {
                call.respondText("Invalid request")
            }
        }
    }
}