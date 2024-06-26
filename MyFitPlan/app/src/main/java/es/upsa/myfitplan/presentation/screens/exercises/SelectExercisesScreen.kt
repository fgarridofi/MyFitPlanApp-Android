package es.upsa.myfitplan.presentation.screens.exercises

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import es.upsa.myfitplan.BuildConfig
import es.upsa.myfitplan.domain.model.exercise.MyFitPlanExercise
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens
import es.upsa.myfitplan.presentation.widgets.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectExercisesScreen(navController: NavController, exercisesviewModel: ExercisesViewModel = hiltViewModel(), selectedExercisesViewModel: SelectedExercisesViewModel = hiltViewModel()) {

    val exercises by exercisesviewModel.exercises.collectAsState(initial = emptyList())
    val selectedExercises = remember { mutableStateListOf<MyFitPlanExercise>() }
    val searchQuery = rememberSaveable { mutableStateOf("") }

    val isLoading by exercisesviewModel.isLoading.collectAsState()
    val exercisesLimit = BuildConfig.ROUTINE_EXERCISES_LIMIT
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Exercises") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Cancel")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                        selectedExercises.forEach { exercise ->
                            Log.d("SelectedExercise", "Exercise: ${exercise.name}, Target: ${exercise.bodyPart}")
                        }
                        selectedExercisesViewModel.setSelectedExercises(selectedExercises)
                        navController.navigate(MyFitPlanScreens.NewRoutineScreen.name)
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
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchBar(
                searchQueryState = searchQuery,
                placeHolder = "Search Exercises...",
                onSearch = { query ->
                    if (query != null && query.isNotEmpty()) {
                        exercisesviewModel.getExercisesByName(query)
                    } else {
                        exercisesviewModel.exercises
                    }
                }
            )
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp))
            } else {
                LazyColumn {
                    items(exercises) { exercise ->
                        ExerciseItem(exercise = exercise, selectedExercises = selectedExercises, navController = navController, exercisesLimit = exercisesLimit, showAlert = { showAlert = true })
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
                }
            }
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Limit Reached") },
            text = { Text("This feature is only available in the premium version.") },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ExerciseItem(
    exercise: MyFitPlanExercise,
    selectedExercises: MutableList<MyFitPlanExercise>,
    navController: NavController,
    exercisesLimit: Int,
    showAlert: () -> Unit
) {
    val isSelected = selectedExercises.contains(exercise)
    val offset by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .offset(x = offset)
            .clickable { toggleExerciseSelection(exercise, selectedExercises, exercisesLimit, showAlert) },

        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = isSelected) {
            Box(
                modifier = Modifier
                    .offset(x = (-8).dp)
                    .size(width = 4.dp, height = 80.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }

        // Load the image from the URL
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory()) // Use ImageDecoder for Android P and above
                } else {
                    add(GifDecoder.Factory()) // Use GifDecoder for versions below Android P
                }
            }
            .build()

        ImageComponent(
            imageUrl = exercise.gifUrl,
            imageLoader = imageLoader,
            contentDescription = "GIF for testing"
        )

        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .weight(1f)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.size(4.dp))
            Text("Target: ${exercise.target}", style = MaterialTheme.typography.bodyMedium)
        }

        Column {
            Button(onClick = { navController.navigate("${MyFitPlanScreens.ExerciseDetailScreen.name}/${exercise.id}") }, colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)) {
                Text(text = "Details", style = MaterialTheme.typography.bodyMedium)
            }
            Button(onClick = { toggleExerciseSelection(exercise, selectedExercises, exercisesLimit, showAlert) }) {
                Text(text = if (isSelected) "Deselect" else "Select", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ImageComponent(imageUrl: String, imageLoader: ImageLoader, contentDescription: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl, imageLoader = imageLoader),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(6.dp)),
        contentScale = ContentScale.Crop
    )
}

private fun toggleExerciseSelection(
    exercise: MyFitPlanExercise,
    selectedExercises: MutableList<MyFitPlanExercise>,
    exercisesLimit: Int,
    showAlert: () -> Unit
) {
    if (selectedExercises.contains(exercise)) {
        selectedExercises.remove(exercise)
    } else {
        if (selectedExercises.size < exercisesLimit) {
            selectedExercises.add(exercise)
        } else {
            showAlert()
        }
    }
}
