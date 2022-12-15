package com.developer.mymarket.di

import android.content.Context
import androidx.room.Room
import com.developer.mymarket.model.db.AppDatabase
import com.developer.mymarket.model.net.createApiService
import com.developer.mymarket.model.repository.cart.CartRepository
import com.developer.mymarket.model.repository.cart.CartRepositoryImpl
import com.developer.mymarket.model.repository.comment.CommentRepository
import com.developer.mymarket.model.repository.comment.CommentRepositoryImpl
import com.developer.mymarket.model.repository.product.ProductRepository
import com.developer.mymarket.model.repository.product.ProductRepositoryImpl
import com.developer.mymarket.model.repository.user.UserRepository
import com.developer.mymarket.model.repository.user.UserRepositoryImpl
import com.developer.mymarket.ui.features.cart.CategoryViewModel
import com.developer.mymarket.ui.features.category.CartViewModel
import com.developer.mymarket.ui.features.main.MainViewModel
import com.developer.mymarket.ui.features.product.ProductViewModel
import com.developer.mymarket.ui.features.profile.ProfileViewModel
import com.developer.mymarket.ui.features.signIn.SignInViewModel
import com.developer.mymarket.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {

    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build() }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get() , get()) }

 viewModel { CategoryViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ProductViewModel(get(), get(), get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainViewModel(get(), get() , isNetConnected) }
    viewModel { CartViewModel(get() , get()) }

}