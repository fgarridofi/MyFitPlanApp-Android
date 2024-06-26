package es.upsa.myfitplan.presentation.screens.homeRoutine

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.domain.model.routine.Routine

import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.DpOffset
import androidx.hilt.navigation.compose.hiltViewModel
import es.upsa.myfitplan.R
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens
import es.upsa.myfitplan.ui.theme.MyFitPlanTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoutineScreen(navController: NavController) {
    MyFitPlanTheme {
        val viewModel: RoutineViewModel = hiltViewModel()
        val listOfRoutines by viewModel.routines.collectAsState()

        Scaffold(
            topBar = { MyTopAppBar(navController) }
        ) { paddingValues ->
            if (listOfRoutines.isEmpty()) {
                NoRoutinesView(paddingValues, navController)
            } else {
                RoutinesListView(paddingValues, listOfRoutines, navController, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController) {
    TopAppBar(
        title = { Text("My Routines") },
        actions = {
            IconButton(onClick = { navController.navigate(MyFitPlanScreens.SelectExerciseScreen.name) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary

        )
    )
}

    @Composable
    private fun NoRoutinesView(paddingValues: PaddingValues, navController: NavController) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Text("No routines yet!!", style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface))
            Image(
                painter = painterResource(id = R.drawable.gimnasio),
                contentDescription = "Add Routine",
                modifier = Modifier.padding(16.dp),
                colorFilter = (ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface))

            )
            Button(onClick = { navController.navigate(MyFitPlanScreens.SelectExerciseScreen.name) }) {
                Text("Add New Routine")
            }

        }
    }

    @Composable
    private fun RoutinesListView(
        paddingValues: PaddingValues,
        listOfRoutines: List<Routine>,
        navController: NavController,
        viewModel: RoutineViewModel
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(listOfRoutines) { routine ->
                RoutineCard(routine, navController, viewModel)
            }
        }
    }

@Composable
private fun RoutineCard(routine: Routine, navController: NavController, viewModel: RoutineViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val iconButtonSize = 48.dp
    val offsetDropdownY = iconButtonSize / 5

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("${MyFitPlanScreens.RoutineDetailScreen.name}/${routine.id}") }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(routine.name, style = MaterialTheme.typography.headlineMedium)
                Text("Number of exercises: ${routine.exercises?.size}", style = MaterialTheme.typography.bodySmall)
                val uniqueTargets = routine.exercises?.map { it.bodyPart }?.toSet()?.joinToString(separator = ", ")
                Text("Target muscles: $uniqueTargets", style = MaterialTheme.typography.bodySmall)

            }

            Box {
                IconButton(
                    onClick = { expanded = true }
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(x = 0.dp, y = offsetDropdownY)
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete Routine") },
                        leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Delete") },
                        onClick = {
                            expanded = false
                            viewModel.deleteRoutine(routine.id)
                        }
                    )
                }
            }
        }
    }
}

