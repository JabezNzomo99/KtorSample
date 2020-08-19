package com.demo.service

import com.demo.models.*
import com.demo.service.DatabaseFactory.dbQuery
import com.demo.util.InvalidProductCategoryException
import com.google.gson.Gson
import redis.clients.jedis.Jedis
import java.util.*
import java.util.logging.Logger


class ProductService(private val jedis:Jedis) {
    suspend fun createProduct(newProduct: PostProduct) : ProductResponse{
        return dbQuery{
            val product = Product.new {
                    productName = newProduct.productName
                    productCategory = newProduct.productCategory
                    productCode = newProduct.productCode
                }
            clearCache()
            product.toProductResponse()
        }
    }

    suspend fun getProducts(): List<ProductResponse>{
        val gson = Gson()
        return dbQuery {
            val cache : ProductReponseCache? = gson.fromJson(jedis.get("products"), ProductReponseCache::class.java)
            if(cache != null && !cache.cachedProducts.isNullOrEmpty()) {
                Logger.getAnonymousLogger().info("Retreived from cache")
                return@dbQuery cache.cachedProducts
            }
            val products = Product.all().map {
                it.toProductResponse()
            }
            jedis.set("products", gson.toJson(ProductReponseCache(products)))
            products
        }
    }

    suspend fun getProductsByCategory(productCategory:String){
        if(!productCategory.equals("FFV") || !productCategory.equals("FMCG")) throw InvalidProductCategoryException("${productCategory} is not a valid product category")
    }

    suspend fun createProductItem(postProductItem: PostProductItem) : ProductItemResponse{
        return dbQuery {
            val retrievedProduct = Product.findById(UUID.fromString(postProductItem.productId))
                ?: throw InvalidProductCategoryException("Product with ${postProductItem.productId} not found")
            val productItem = ProductItem.new() {
                productItemName = postProductItem.productItemName
                uom = postProductItem.uom
                product = retrievedProduct
            }
            clearCache()
            productItem.toProductItemResponse()
        }
    }

    suspend fun getProductItems():List<ProductItemResponse> = dbQuery {
        val gson = Gson()
        val cache : ProductItemResponseCache? = gson.fromJson(jedis.get("productitems"), ProductItemResponseCache::class.java)
        if(cache != null && !cache.cachedProductItems.isNullOrEmpty()) {
            Logger.getAnonymousLogger().info("Retreived from cache")
            return@dbQuery cache.cachedProductItems
        }
        val productItems = ProductItem.all().map {
            it.toProductItemResponse()
        }
        jedis.set("productitems", gson.toJson(ProductItemResponseCache(productItems)))
        productItems
    }

    suspend fun getProductById(productId:String?) : ProductResponse = dbQuery {
        val product = Product.findById(UUID.fromString(productId))
            ?: throw InvalidProductCategoryException("Product with $productId not found")
        product.toProductResponse()
    }

    suspend fun deleteProduct(productId: String?){
        return dbQuery {
            val product = Product.findById(UUID.fromString(productId))
                ?: throw InvalidProductCategoryException("Product with $productId not found")
            clearCache()
            product.delete()
        }
    }

    suspend fun updateProduct(productId: String?, updateProduct: UpdateProduct) : ProductResponse{
        return dbQuery {
            val product = Product.findById(UUID.fromString(productId))
                ?: throw InvalidProductCategoryException("Product with $productId not found")
            product.apply {
                updateProduct.productName?.let { this.productName = updateProduct.productName }
                updateProduct.productCode?.let { this.productCode = updateProduct.productCode }
                updateProduct.productCategory?.let { this.productCategory = updateProduct.productCategory }
            }
            clearCache()
            product.toProductResponse()
        }
    }

    private fun clearCache(){
        jedis.del("products")
        jedis.del("productitems")
    }

}

class ProductReponseCache(val cachedProducts : List<ProductResponse>)
class ProductItemResponseCache(val cachedProductItems : List<ProductItemResponse>)

