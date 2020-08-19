package com.demo.models


import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import java.util.*
import org.joda.time.DateTime
import java.io.Serializable

class Product(id: EntityID<UUID>) : UUIDEntity(id){
    companion object : UUIDEntityClass<Product>(Products)
    var productName: String by Products.productName
    var productCode: String by Products.productCode
    var productCategory: String by Products.productCategory
    val productItems by ProductItem referrersOn ProductItems.product
    var createdAt by Products.createdAt
    var updatedAt by Products.updatedAt
}

data class PostProduct(val productName: String, val productCode: String, val productCategory: String)

data class UpdateProduct(val productName: String?, val productCode: String?, val productCategory: String?)


data class ProductResponse(
        val id:String,
        val productName: String,
        val productCode: String,
        val productCategory : String,
        val productItems : List<ProductItemResponse>,
        val createdAt: String,
        val updatedAt:String
):Serializable


object Products : UUIDTable(){
    val productName = varchar("productName", 255)
    val productCode = varchar("productCode", 255)
    val productCategory = varchar("productCategory", 255)
    val createdAt = datetime("createdAt").default(DateTime.now())
    val updatedAt = datetime("updatedAt").default(DateTime.now())
}

fun Product.toProductResponse():ProductResponse{
    return ProductResponse(
        this.id.toString(),
        this.productName,
        this.productCode,
        this.productCategory,
        this.productItems.toList().map {
            it.toProductItemResponse()
        },
        this.createdAt.toString(),
        this.updatedAt.toString()
    )
}