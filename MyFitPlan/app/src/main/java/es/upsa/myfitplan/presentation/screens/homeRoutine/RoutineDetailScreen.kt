package es.upsa.myfitplan.presentation.screens.homeRoutine

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import es.upsa.myfitplan.domain.model.settings.WeightUnit
import es.upsa.myfitplan.presentation.navigation.MyFitPlanScreens
import es.upsa.myfitplan.presentation.screens.settings.SettingsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routineId: Int,
    navController: NavController,
    routineViewModel: RoutineViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val routine by routineViewModel.selectedRoutine.collectAsState()
    var weightUnit by remember { mutableStateOf(WeightUnit.Kg) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        settingsViewModel.weightUnitFlow.collect { unit ->
            weightUnit = unit
        }
    }

    LaunchedEffect(Unit) {
        routineViewModel.getRoutineById(routineId)
        Log.d("RoutineDetailScreen", "Getting routine with id $routineId")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = routine?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyColumn {
                items(routine?.exercises ?: emptyList()) { exercise ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 15.dp, vertical = 8.dp)
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

                                Spacer(modifier = Modifier.padding(end = 8.dp))

                                Text(
                                    text = exercise.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = { navController.navigate("${MyFitPlanScreens.ExerciseDetailScreen.name}/${exercise.id}") },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "View Details")
                                }
                            }

                            Divider()

                            HeaderSeriesComponent(weightUnit)

                            exercise.series.forEach { series ->
                                val keyboardController = LocalSoftwareKeyboardController.current
                                val seriesNum = series.num
                                var editedRepsValue by remember { mutableStateOf(series.reps ?: "0") }
                                var editedWeightValue by remember { mutableStateOf(series.weight ?: "0") }
                                var hasValueChanged by remember { mutableStateOf(false) }

                                Log.d("RoutineDetailScreen", "Value series $seriesNum  reps $editedRepsValue  weight $editedWeightValue")

                                LaunchedEffect(series) {
                                    editedRepsValue = series.reps ?: "0"
                                    editedWeightValue = series.weight ?: "0"
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = series.num.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.Center
                                    )


                                    TextField(
                                        value = editedRepsValue,
                                        onValueChange = { editedRepsValue = it
                                            hasValueChanged = true
                                        },
                                        label = { Text("Reps") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(3f),
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                            }
                                        )
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                                    TextField(
                                        value = editedWeightValue,
                                        onValueChange = { editedWeightValue = it
                                            hasValueChanged = true
                                        },
                                        label = { Text("Weight (${weightUnit.name})") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(3f),
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondary,
                                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                            }
                                        )
                                    )

                                    if (hasValueChanged) {
                                        LaunchedEffect(editedRepsValue, editedWeightValue) {
                                            Log.d(
                                                "RoutineDetailScreen",
                                                "Updating series $seriesNum with reps $editedRepsValue and weight $editedWeightValue"
                                            )
                                            routineViewModel.updateSeries(
                                                routineId,
                                                exercise.id,
                                                seriesNum,
                                                editedRepsValue,
                                                editedWeightValue
                                            )
                                        }
                                    }
                                }
                            }

                            AddSerieButtonComponent(
                                onClick = {
                                    routineViewModel.addSeriesToExercise(routineId, exercise.id)
                                    Log.d("RoutineDetailScreen", "Adding series to exercise ${exercise.id} in routine $routineId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun HeaderSeriesComponent(weightUnit: WeightUnit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Series",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Reps",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Weight (${weightUnit.name})",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            modifier = Modifier.weight(3f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AddSerieButtonComponent(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = "Add Series",
            color = Color.White
        )
    }
}

@Composable
fun ImageComponentDetail(imageUrl: String, imageLoader: ImageLoader, contentDescription: String) {
    Image(
        painter = rememberAsyncImagePainter(model = imageUrl, imageLoader = imageLoader),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(6.dp)),
        contentScale = ContentScale.Crop
    )
}

