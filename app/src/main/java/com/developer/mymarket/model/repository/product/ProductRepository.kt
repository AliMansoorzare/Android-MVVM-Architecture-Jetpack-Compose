package com.developer.mymarket.model.repository.product

import com.developer.mymarket.model.data.Ads
import com.developer.mymarket.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected :Boolean): List<Product>
    suspend fun getAllAds(isInternetConnected :Boolean): List<Ads>
    suspend fun getAllProductsByCategory(category :String) :List<Product>
    suspend fun getProductById(productId :String) : Product

}