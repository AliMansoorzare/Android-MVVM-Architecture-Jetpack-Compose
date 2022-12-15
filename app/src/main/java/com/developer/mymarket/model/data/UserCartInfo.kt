package com.developer.mymarket.model.data

import com.developer.mymarket.model.data.Product

data class UserCartInfo(
    val success: Boolean,
    val productList: List<Product>,
    val message: String,
    val totalPrice: Int
)