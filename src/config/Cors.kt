package com.demo.config

import io.ktor.features.*
import io.ktor.http.*

fun CORS.Configuration.cors(){
    method(HttpMethod.Options)
    method(HttpMethod.Put)
    method(HttpMethod.Delete)
    method(HttpMethod.Patch)
    header(HttpHeaders.Authorization)
    header("MyCustomHeader")
    allowCredentials = true
    allowSameOrigin= true
    anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
}