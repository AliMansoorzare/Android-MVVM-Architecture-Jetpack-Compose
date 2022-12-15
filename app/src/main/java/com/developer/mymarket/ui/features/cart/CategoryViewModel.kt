package com.developer.mymarket.ui.features.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.mymarket.model.data.Product
import com.developer.mymarket.model.repository.product.ProductRepository
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    val dataProducts = mutableStateOf<List<Product>>(listOf())

    fun loadDataByCategory(category: String) {

        viewModelScope.launch {

            val dataFromLocal = productRepository.getAllProductsByCategory(category)
            dataProducts.value = dataFromLocal

        }

    }

}