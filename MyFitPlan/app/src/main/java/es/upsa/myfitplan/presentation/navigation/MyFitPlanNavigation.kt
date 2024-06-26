package es.upsa.myfitplan.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import es.upsa.myfitplan.presentation.screens.exercises.ExercisesViewModel
import es.upsa.myfitplan.presentation.screens.exercises.SelectExercisesScreen
import es.upsa.myfitplan.presentation.screens.exercises.ExerciseDetailScreen
import es.upsa.myfitplan.presentation.screens.exercises.SearchExerciseScreen
import es.upsa.myfitplan.presentation.screens.exercises.SelectedExercisesViewModel
import es.upsa.myfitplan.presentation.screens.homeRoutine.HomeRoutineScreen
import es.upsa.myfitplan.presentation.screens.homeRoutine.RoutineDetailScreen
import es.upsa.myfitplan.presentation.screens.homeRoutine.RoutineViewModel
import es.upsa.myfitplan.presentation.screens.homeRoutine.newRoutineScreen
import es.upsa.myfitplan.presentation.screens.settings.SettingsScreen
import es.upsa.myfitplan.presentation.screens.splash.SplashScreen
import es.upsa.myfitplan.presentation.screens.settings.SettingsViewModel
import es.upsa.myfitplan.presentation.widgets.BottomNavigationBarComponent

@Composable
fun MyFitPlanNavigation() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedExercisesViewModel = hiltViewModel<SelectedExercisesViewModel>()
    val routineViewModel = hiltViewModel<RoutineViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val exercisesViewModel = hiltViewModel<ExercisesViewModel>()


    Scaffold(
        bottomBar = {
            if (currentRoute != MyFitPlanScreens.SplashScreen.name) {
                BottomNavigationBarComponent(navController = navController)
            }

        }
    ) {contentPadding ->
        NavHost(navController = navController, startDestination = MyFitPlanScreens.SplashScreen.name,modifier = Modifier.padding(contentPadding)) {

            composable(MyFitPlanScreens.SplashScreen.name) {
                SplashScreen(navController)
            }

            composable(MyFitPlanScreens.HomeRoutinesScreen.name) {
                HomeRoutineScreen(navController)
            }

            composable(MyFitPlanScreens.NewRoutineScreen.name) {
                newRoutineScreen(navController, selectedExercisesViewModel)
            }

            composable(MyFitPlanScreens.SettingsScreen.name) {
                SettingsScreen(navController, settingsViewModel)
            }

            composable(route = "${MyFitPlanScreens.RoutineDetailScreen.name}/{routineId}") { backStackEntry ->
                val routineId = backStackEntry.arguments?.getString("routineId") ?: ""
                RoutineDetailScreen(routineId.toInt(), navController, routineViewModel,settingsViewModel)
            }


            composable(route = "${MyFitPlanScreens.ExerciseDetailScreen.name}/{ExerciseId}") { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getString("ExerciseId") ?: ""
                ExerciseDetailScreen(exerciseId, navController, exercisesViewModel)
            }


            composable(MyFitPlanScreens.SelectExerciseScreen.name) {
                SelectExercisesScreen(navController,exercisesViewModel, selectedExercisesViewModel)
            }

            composable(MyFitPlanScreens.SearchExercisesScreen.name) {
                SearchExerciseScreen(navController, exercisesViewModel)
            }
        }
    }


}