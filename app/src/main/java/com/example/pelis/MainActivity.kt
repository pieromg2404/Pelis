package com.example.pelis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pelis.UIScreen.ListMovieScreen
import com.example.pelis.UIScreen.LoginScreen
import com.example.pelis.UIScreen.MovieDetailScreen
import com.example.pelis.ui.theme.PelisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PelisTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavGraphDestinations.Login) {
        composable(NavGraphDestinations.Login) {
            LoginScreen(onLogin = {
                navController.navigate(NavGraphDestinations.Welcome)
            })
        }
        composable(NavGraphDestinations.Welcome) {
            ListMovieScreen(navController)
        }
        composable(NavGraphDestinations.MovieDetail) { backStackEntry ->
            MovieDetailScreen()
        }
    }
}

object NavGraphDestinations {
    const val Login = "login"
    const val Welcome = "welcome"
    const val MovieDetail = "movieDetail"

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PelisTheme {
        MyApp()
    }
}