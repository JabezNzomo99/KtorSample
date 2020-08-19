package com.demo.config

import com.demo.util.AuthenticationException
import com.demo.util.AuthorizationException
import com.demo.util.InvalidProductCategoryException
import com.demo.util.InvalidProductIdException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.statusPages(){
    exception<AuthenticationException> { cause ->
        call.respond(HttpStatusCode.Unauthorized)
    }
    exception<AuthorizationException> { cause ->
        call.respond(HttpStatusCode.Forbidden)
    }
    exception<InvalidProductCategoryException> {cause->
        call.respond(HttpStatusCode.BadRequest, Error(HttpStatusCode.BadRequest.value, HttpStatusCode.BadRequest.description, cause.message.toString()))
    }
    exception<InvalidProductIdException> { cause->
        call.respond(HttpStatusCode.BadRequest, Error(HttpStatusCode.BadRequest.value, HttpStatusCode.BadRequest.description, cause.message.toString()))
    }
}
data class Error(val errorCode:Int, val errorDescription: String, val errorMessage:String)