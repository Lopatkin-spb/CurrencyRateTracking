package com.example.currencyratetracking.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.currencyratetracking.app.CrtApp
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.core.AbstractActivity
import com.example.currencyratetracking.currencies.di.CurrenciesComponent
import com.example.currencyratetracking.currencies.di.CurrenciesComponentProvider
import com.example.currencyratetracking.di.app.activity.MainComponent
import com.example.currencyratetracking.presentation.ModuleTag.TAG_LOG
import com.example.currencyratetracking.ui_theme.CurrencyRateTrackingTheme
import javax.inject.Inject


class MainActivity : AbstractActivity(), CurrenciesComponentProvider {

    @Inject
    lateinit var logger: BaseLogger

    private lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as CrtApp).getAppComponent().getMainComponent().create()
        mainComponent.inject(this)
        super.onCreate(savedInstanceState)
        logger.d(TAG_LOG, "$NAME_FULL started")
        setContent {
            CurrencyRateTrackingTheme {
                Content()
            }
        }
    }

    override fun onStart() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onStart()
    }

    override fun onRestart() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onRestart()
    }

    override fun onResume() {
        logger.i(TAG_LOG, "$NAME_FULL started")
        super.onResume()
    }

    override fun onPause() {
        logger.i(TAG_LOG, "$NAME_FULL started")
        super.onPause()
    }

    override fun onStop() {
        logger.v(TAG_LOG, "$NAME_FULL started")
        super.onStop()
    }

    override fun onDestroy() {
        logger.d(TAG_LOG, "$NAME_FULL started")
        super.onDestroy()
    }

    fun getMainComponent(): MainComponent = mainComponent

    override fun provideCurrenciesComponent(): CurrenciesComponent {
        return getMainComponent().currenciesComponent().create()
    }

}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Scaffold(
            bottomBar = {
                BottomNavigationComponent(navController = navController, destinationList = AppDestination.toList)
            },
        ) { innerPadding ->
            BottomNavHost(
                modifier = modifier.padding(innerPadding),
                navController = navController,
            )
        }
    }
}


@Composable
private fun BottomNavigationComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    destinationList: List<AppDestination>,
) {

    Column(modifier = Modifier.wrapContentSize()) {

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outline))
        NavigationBar(
            modifier = Modifier.height(68.dp).fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            val currentBackstack by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackstack?.destination

            destinationList.forEach { destination ->

                val isSelected = currentDestination?.hierarchy?.any { it.route == destination.uniqueTag } == true
                val fontWeightSelected = if (isSelected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Medium
                }

                NavigationBarItem(
                    icon = { Icon(painter = painterResource(destination.iconId), contentDescription = null) },
                    label = {
                        Text(
                            text = stringResource(destination.titleId),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = fontWeightSelected,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.secondary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    alwaysShowLabel = true,
                    selected = isSelected,
                    onClick = { navController.navigateSingleTopTo(route = destination.uniqueTag) },
                )
            }
        }
    }
}


//todo: bug-dont work
@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    CurrencyRateTrackingTheme {
        Content()
    }
}