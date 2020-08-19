package com.demo

import com.demo.config.api
import com.demo.config.cors
import com.demo.config.statusPages
import com.demo.service.DatabaseFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.*
import io.ktor.locations.*
import org.slf4j.event.Level
import java.time.LocalDateTime


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    install(DefaultHeaders){
        header("api_version", "v1")
        header("date_time", LocalDateTime.now().toString())
    }
    install(Locations)
    //Enable communication with JS clients
    install(CORS) {
        cors()
    }
    //enable logging for debugging
    install(CallLogging){
        level = Level.INFO
    }
    install(ContentNegotiation) {
        gson {
        }
    }
    DatabaseFactory.init()
    routing {
        install(StatusPages) {
            statusPages()
        }
        api()

    }
}
