package com.example.test_task

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test_task.data.RoomDB
import com.example.test_task.ui.screens.search.SearchScreen
import com.example.test_task.ui.screens.selected_bin.SelectedBinScreen
import com.example.test_task.ui.theme.Test_taskTheme

const val BIN_LENGTH = 8


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            RoomDB::class.java, "myDB"
        ).build()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Destinations.SEARCH.name
            ) {
                composable(Destinations.SEARCH.name) {
                    SearchScreen(navController, db)
                }
                composable(route = "${Destinations.SELECTED_BIN}/{${Arguments.BIN.name}}",
                    arguments = listOf(navArgument(Arguments.BIN.name) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                    )
                ) {
                    SelectedBinScreen(navController)
                }
            }
        }
    }
}

fun goByUri(uri: Uri, action: String, context: Context) {
    val intent = Intent(action, uri)
    context.startActivity(intent)
}

fun goByLink(link: String, context: Context) {
    val uri = if (!link.startsWith("http://") && !link.startsWith("https://")) {
        Uri.parse("http://$link");
    }else{
        Uri.parse(link)
    }
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

enum class Destinations{
    SEARCH,
    SELECTED_BIN
}
enum class Arguments{
    BIN,
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Test_taskTheme {
        Greeting("Android")
    }
}