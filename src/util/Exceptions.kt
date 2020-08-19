package com.demo.util

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
class InvalidProductCategoryException(override val message: String?) : RuntimeException(message)
class InvalidProductIdException(override val message: String?) : RuntimeException(message)