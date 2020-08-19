package com.demo.models

import com.google.gson.annotations.SerializedName
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.io.Serializable

object ProductItems : IntIdTable(){
    @SerializedName("key")
    val product = reference("products", Products)
    val productItemName : Column<String> = varchar("productItemName", 255)
    val uom : Column<String> = varchar("uom", 255)
    val createdAt = datetime("createdAt").default(DateTime.now())
    val updatedAt = datetime("updatedAt").default(DateTime.now())
}

class ProductItem(id:EntityID<Int>):IntEntity(id){
    companion object : IntEntityClass<ProductItem>(ProductItems)
    var productItemName by ProductItems.productItemName
    var uom by ProductItems.uom
    var product by Product referencedOn ProductItems.product
    var createdAt by ProductItems.createdAt
    var updatedAt by ProductItems.updatedAt
}

data class PostProductItem(val productId : String, val productItemName : String, val uom:String)

data class ProductItemResponse(
        val productItemId: String,
        val productId: String,
        val productItemName: String,
        val uom: String,
        val createdAt:String,
        val updatedAt:String
):Serializable


fun ProductItem.toProductItemResponse() : ProductItemResponse{
    return ProductItemResponse(this.id.toString(), this.product.id.toString(), this.productItemName, this.uom, this.createdAt.toString(), this.updatedAt.toString())
}

