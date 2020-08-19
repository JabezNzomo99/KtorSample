package com.demo.service

import com.demo.models.ProductItems
import com.demo.models.Products
import com.impossibl.postgres.jdbc.PGDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

object DatabaseFactory{
    fun init(){
        Database.connect(postgreSQL())
        transaction {
            create(Products, ProductItems)
        }
    }

    private fun postgreSQL():PGDataSource{
        val pgDataSource = PGDataSource().apply {
            serverName = "localhost"
            databaseName = "jabez"
            user = "jabez"
            password = ""
        }
        return pgDataSource
    }

    suspend fun <T> dbQuery(block : ()-> T) = withContext(Dispatchers.IO){
        transaction {
            block()
        }
    }
}
