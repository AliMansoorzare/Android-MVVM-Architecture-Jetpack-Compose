package com.developer.mymarket.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.mymarket.model.data.Comment
import com.developer.mymarket.model.repository.cart.CartRepository
import com.developer.mymarket.model.repository.comment.CommentRepository
import com.developer.mymarket.model.repository.product.ProductRepository
import com.developer.mymarket.util.EMPTY_PRODUCT
import com.developer.mymarket.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)

    fun loadData(productId: String, isInternetConnected: Boolean) {

        loadProductFromCache(productId)

        if (isInternetConnected) {
            loadAllComments(productId)
            loadBadgeNumber()
        }

    }

    private fun loadProductFromCache(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }

    }

    private fun loadBadgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }

    }

    private fun loadAllComments(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productId)
        }

    }

    fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, text, IsSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productId)
        }
    }

    fun addProductToCart(productId: String , AddingToCartResult :(String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            isAddingProduct.value = true

            val result = cartRepository.addToCart(productId)
            delay(500)

            isAddingProduct.value = false

            if(result) {
                AddingToCartResult.invoke("Product Added to Cart")
            } else {
                AddingToCartResult.invoke("Product not Added")
            }


        }

    }

}