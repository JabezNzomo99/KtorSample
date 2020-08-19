package com.demo.api

import com.demo.models.PostProduct
import com.demo.models.PostProductItem
import com.demo.models.UpdateProduct
import com.demo.service.ProductService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.eclipse.jetty.http.HttpStatus

fun Route.products(productService: ProductService){
    post("/products"){
        val newProduct = call.receive<PostProduct>()
        val product = productService.createProduct(newProduct)
        call.respond(product)
    }

    get("/products"){
        call.respond(productService.getProducts())
    }

    get("/products/{id}") {
        val productId = call.parameters["id"]
        val product = productService.getProductById(productId)
        call.respond(product)
    }

    post ("/productItems"){
        val postProductItem = call.receive<PostProductItem>()
        call.respond(productService.createProductItem(postProductItem))
    }

    get("/productItems"){
        call.respond(productService.getProductItems())
    }

    delete("products/{productId}") {
        val productId = call.parameters["productId"]
        productService.deleteProduct(productId)
        call.respond(HttpStatusCode.OK)
    }

    put("products/{productId}") {
        val productId = call.parameters["productId"]
        val updateProduct = call.receive<UpdateProduct>()
        call.respond(productService.updateProduct(productId, updateProduct))
    }

}