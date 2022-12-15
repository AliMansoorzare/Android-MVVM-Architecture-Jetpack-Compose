package com.developer.mymarket.model.repository.comment

import com.developer.mymarket.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId :String) :List<Comment>
    suspend fun addNewComment(productId: String , text :String , IsSuccess :(String) -> Unit)

}