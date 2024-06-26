package es.upsa.myfitplan.presentation.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import es.upsa.myfitplan.R
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens


@Composable
fun BottomNavigationBarComponent(navController: NavController) {
    val items = listOf(
        TabBarItem("My Routines", MyFitPlanScreens.HomeRoutinesScreen.name, Icons.Filled.Home, Icons.Outlined.Home),
        TabBarItem("Exercises", MyFitPlanScreens.SearchExercisesScreen.name, painterResource(id = R.drawable.pesa_filled), painterResource(id = R.drawable.pesa)),
        TabBarItem("Settings", MyFitPlanScreens.SettingsScreen.name, Icons.Filled.Settings, Icons.Outlined.Settings),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TabView(
        tabBarItems = items,
        navController = navController,
        currentRoute = currentRoute
    )
}