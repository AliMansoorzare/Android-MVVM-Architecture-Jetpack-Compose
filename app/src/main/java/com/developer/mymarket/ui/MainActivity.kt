package com.developer.mymarket.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import com.developer.mymarket.di.myModules
import com.developer.mymarket.model.repository.TokenInMemory
import com.developer.mymarket.model.repository.user.UserRepository
import com.developer.mymarket.ui.features.IntroScreen
import com.developer.mymarket.ui.features.cart.CategoryScreen
import com.developer.mymarket.ui.features.category.CartScreen
import com.developer.mymarket.ui.features.main.MainScreen
import com.developer.mymarket.ui.features.product.ProductScreen
import com.developer.mymarket.ui.features.profile.ProfileScreen
import com.developer.mymarket.ui.features.signIn.SignInScreen
import com.developer.mymarket.ui.features.signUp.SignUpScreen
import com.developer.mymarket.ui.theme.BackgroundMain
import com.developer.mymarket.ui.theme.MainAppTheme
import com.developer.mymarket.util.KEY_CATEGORY_ARG
import com.developer.mymarket.util.KEY_PRODUCT_ARG
import com.developer.mymarket.util.MyScreens
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MainAppTheme {
                    Surface(color = BackgroundMain, modifier = Modifier.fillMaxSize()) {

                        val userRepository : UserRepository = get()
                        userRepository.loadToken()

                        DuniBazaarUi()

                    }
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DuniBazaarUi()
        }
    }
}

@Composable
fun DuniBazaarUi() {
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.MainScreen.route) {

        composable(MyScreens.MainScreen.route) {

            if(TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroScreen()
            }

        }

        composable(
            route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {

            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, "null"))

        }

        composable(
            route = MyScreens.CategoryScreen.route + "/" + "{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) {
                type = NavType.StringType
            })
        ) {
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, "null"))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }

    }

}
