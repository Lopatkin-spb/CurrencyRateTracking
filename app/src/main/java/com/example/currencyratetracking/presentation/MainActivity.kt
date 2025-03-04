package com.example.currencyratetracking.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.currencyratetracking.app.CrtApp
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractActivity
import com.example.currencyratetracking.di.app.activity.MainComponent
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import com.example.currencyratetracking.presentation.theme.CurrencyRateTrackingTheme
import javax.inject.Inject


class MainActivity : AbstractActivity() {

    @Inject
    lateinit var logger: BaseLogger

    private lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as CrtApp).getAppComponent().getMainComponent().create()
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        logger.d(TAG_LOG, "$NAME_CLASS onCreate(): started")
        setContent {
            CurrencyRateTrackingTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//                    Greeting("Android")
                    Content()
                }
            }
        }
    }

    override fun onStart() {
        logger.v(TAG_LOG, "$NAME_CLASS onStart(): started")
        super.onStart()
    }

    override fun onRestart() {
        logger.v(TAG_LOG, "$NAME_CLASS onRestart(): started")
        super.onRestart()
    }

    override fun onResume() {
        logger.i(TAG_LOG, "$NAME_CLASS onResume(): started")
        super.onResume()
    }

    override fun onPause() {
        logger.i(TAG_LOG, "$NAME_CLASS onPause(): started")
        super.onPause()
    }

    override fun onStop() {
        logger.v(TAG_LOG, "$NAME_CLASS onStop(): started")
        super.onStop()
    }

    override fun onDestroy() {
        logger.d(TAG_LOG, "$NAME_CLASS onDestroy(): started")
        super.onDestroy()
    }

    fun getMainComponent(): MainComponent = mainComponent

}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    CurrencyRateTrackingTheme {
//        Greeting("Android")
//    }
//}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
    ) { innerPadding ->
        BottomNavHost(
            modifier = modifier
                .padding(innerPadding),
            navController = navController,
        )
    }
}


@Composable
private fun BottomNavBar(
    navController: NavHostController,
) {
    BottomNavigation(
//        backgroundColor = colorResource(R.color.black),
    ) {
        val currentBackstack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackstack?.destination

        bottomNavDestinations.forEach { destination ->

            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(destination.iconId),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.titleId),
                    )
                },
//                selectedContentColor = colorResource(R.color.blue),
//                unselectedContentColor = colorResource(R.color.gray_6),
                alwaysShowLabel = false,
                selected = currentDestination?.hierarchy?.any { it.route == destination.uniqueTag } == true,
                onClick = { navController.navigateSingleTopTo(route = destination.uniqueTag) },
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Content()
        }
    }
}

//add menu, first screen
// up kotlin ver to 1.9 to top build gradle
