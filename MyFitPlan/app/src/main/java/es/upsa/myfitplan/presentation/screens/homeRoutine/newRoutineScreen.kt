package es.upsa.myfitplan.presentation.screens.homeRoutine

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import es.upsa.myfitplan.domain.model.routine.Routine
import es.upsa.myfitplan.presentation.screens.exercises.SelectedExercisesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newRoutineScreen(navController: NavController, selectedExercisesViewModel: SelectedExercisesViewModel = hiltViewModel() ) {
    var routineName by rememberSaveable  { mutableStateOf("") }
    val viewModel: RoutineViewModel = hiltViewModel()
    var isError by remember { mutableStateOf(false) }
    val selectedExercises = selectedExercisesViewModel.selectedExercises.collectAsState()


    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = { Text("Add new Routine") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                        if (routineName.isNotEmpty() && selectedExercises.value.isNotEmpty()) {
                            viewModel.insertRoutine(Routine(name = routineName, exercises = selectedExercises.value))
                            navController.navigate("HomeRoutinesScreen")
                        }else {
                            isError = true
                        }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary, contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Done")
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                OutlinedTextField(
                    value = routineName,
                    onValueChange = {
                        routineName = it
                        isError = it.isEmpty()
                    },
                    label = { Text(text = "Routine Name") },
                    isError = isError,
                    trailingIcon = {
                        if (isError) {
                            Icon(Icons.Filled.Info, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(38.dp)
                )

                Spacer(modifier = Modifier.padding(7.dp))

                if (selectedExercises.value.isNotEmpty()) {
                    Text("${selectedExercises.value.size} exercises selected:", style = MaterialTheme.typography.titleMedium)
                    Divider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onSurface, thickness = 1.dp)
                    selectedExercises.value.forEach { exercise ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                        ) {

                            val imageLoader = ImageLoader.Builder(LocalContext.current)
                                .components {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        add(ImageDecoderDecoder.Factory()) // Use ImageDecoder for Android P and above
                                    } else {
                                        add(GifDecoder.Factory()) // Use GifDecoder for versions below Android P
                                    }
                                }
                                .build()

                            ImageComponentDetail(
                                imageUrl = exercise.gifUrl,
                                imageLoader = imageLoader,
                                contentDescription = "GIF for testing"
                            )

                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(text = exercise.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Button(
                    onClick = {
                        if (routineName.isNotEmpty() && selectedExercises.value.isNotEmpty()) {
                            viewModel.insertRoutine(Routine(name = routineName, exercises = selectedExercises.value))
                            navController.navigate("HomeRoutinesScreen")
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Create Routine")
                }
            }

        }
    }
}





