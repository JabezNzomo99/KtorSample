package com.demo.config

import com.demo.api.products
import com.demo.service.ProductService
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import redis.clients.jedis.Jedis
import kotlin.random.Random

fun Route.api(){
    val jedis = Jedis("localhost",6379)
    val productService = ProductService(jedis)

    route("/api"){
        get("/jabez"){
            call.respond(mapOf("name" to Random.nextInt()))
        }
        products(productService= productService)
    }
}