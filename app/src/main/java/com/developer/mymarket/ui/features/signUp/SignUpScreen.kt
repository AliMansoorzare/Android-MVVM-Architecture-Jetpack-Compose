package com.developer.mymarket.ui.features.signUp

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import com.developer.mymarket.ui.theme.BackgroundMain
import com.developer.mymarket.ui.theme.Blue
import com.developer.mymarket.ui.theme.MainAppTheme
import com.developer.mymarket.ui.theme.Shapes
import com.developer.mymarket.util.MyScreens
import com.developer.mymarket.util.NetworkChecker
import com.developer.mymarket.util.VALUE_SUCCESS
import serat.mymarket.R

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {

    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            SignUpScreen()
        }
    }

}

@Composable
fun SignUpScreen() {
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }

    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getNavViewModel<SignUpViewModel>()

    clearInputs(viewModel)

    Box {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SignUpLottie()

            MainCardView(navigation, viewModel) {

                viewModel.signUpUser {

                    if (it == VALUE_SUCCESS) {

                        navigation.navigate(MyScreens.MainScreen.route) {
                            popUpTo(MyScreens.MainScreen.route) {
                                inclusive = true
                            }
                        }

                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }
    }
}

@Composable
fun SignUpLottie() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.phoneverification)
    )
    val isPlaying by remember { mutableStateOf(true) }
    val speed by remember {
        mutableStateOf(2f)
    }
    val progress by animateLottieCompositionAsState(
        isPlaying = isPlaying,
        speed = speed,
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier
            .height(210.dp)
            .width(210.dp)
            .padding(5.dp)
            .clickable { }
    )
}

@Composable
fun MainCardView(navigation: NavController, viewModel: SignUpViewModel, SignUpEvent: () -> Unit) {
    val name = viewModel.name.observeAsState("")
    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val confirmPassword = viewModel.confirmPassword.observeAsState("")
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 10.dp,
        shape = Shapes.medium
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                text = "Sign Up",
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )

            MainTextField(
                name.value,
                R.drawable.ic_person,
                "Your Full Name"
            ) { viewModel.name.value = it }
            MainTextField(email.value, R.drawable.ic_email, "Email") { viewModel.email.value = it }
            PasswordTextField(
                password.value,
                R.drawable.ic_password,
                "Password"
            ) { viewModel.password.value = it }
            PasswordTextField(
                confirmPassword.value,
                R.drawable.ic_password,
                "Confirm Password"
            ) { viewModel.confirmPassword.value = it }
            Button(onClick = {
                if (name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confirmPassword.value.isNotEmpty()) {
                    if (password.value == confirmPassword.value) {
                        if (password.value.length >= 8) {
                            if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                if (NetworkChecker(context).isInternetConnected) {
                                    SignUpEvent.invoke()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "please connect to internet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "email format is not true",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "password characters should be more than 8!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "passwords are not the same!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "please write data first...", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.padding(top = 28.dp, bottom = 8.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Register Account"
                )
            }

            Row(
                modifier = Modifier.padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Already have  an account?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {

                    navigation.navigate(MyScreens.SignInScreen.route) {
                        popUpTo(MyScreens.SignUpScreen.route) {
                            inclusive = true
                        }
                    }

                }) { Text("Log In", color = Blue) }

            }


        }

    }


}

@Composable
fun MainTextField(edtValue: String, icon: Int, hint: String, onValueChanges: (String) -> Unit) {

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) }
    )

}

@Composable
fun PasswordTextField(edtValue: String, icon: Int, hint: String, onValueChanges: (String) -> Unit) {
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {

            val image = if (passwordVisible.value) painterResource(R.drawable.ic_invisible)
            else painterResource(R.drawable.ic_visible)

            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier.clickable { passwordVisible.value = !passwordVisible.value }
            )

        }
    )

}

fun clearInputs(viewModel: SignUpViewModel) {
    viewModel.email.value = ""
    viewModel.name.value = ""
    viewModel.password.value = ""
    viewModel.confirmPassword.value = ""
}